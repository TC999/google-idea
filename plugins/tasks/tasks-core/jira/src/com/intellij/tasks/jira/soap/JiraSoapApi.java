package com.intellij.tasks.jira.soap;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.atlassian.connector.commons.jira.soap.axis.JiraSoapService;
import com.atlassian.connector.commons.jira.soap.axis.JiraSoapServiceServiceLocator;
import com.atlassian.theplugin.jira.api.JIRAIssueBean;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.KeyValue;
import com.intellij.tasks.LocalTask;
import com.intellij.tasks.Task;
import com.intellij.tasks.TaskState;
import com.intellij.tasks.impl.TaskUtil;
import com.intellij.tasks.jira.JiraRemoteApi;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.net.HttpConfigurable;
import org.apache.axis.AxisProperties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Legacy SOAP connector restored due to IDEA-120595.
 *
 * @author Mikhail Golubev
 */
public class JiraSoapApi extends JiraRemoteApi {

  private static final Logger LOG = Logger.getInstance(JiraSoapApi.class);
  private boolean myJira4 = true;

  public JiraSoapApi(@NotNull JiraRepository repository) {
    super(repository);
  }

  @NotNull
  @Override
  public List<Task> findTasks(String query, int max) throws Exception {
    StringBuilder url = new StringBuilder(myRepository.getUrl());
    url.append("/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?");
    url.append("tempMax=").append(max);
    url.append("&assignee=").append(TaskUtil.encodeUrl(myRepository.getUsername()));
    url.append("&reset=true");
    url.append("&sorter/field=updated");
    url.append("&sorter/order=DESC");
    url.append("&pager/start=0");
    return processRSS(url.toString(), login());
  }

  private List<Task> processRSS(String url, HttpClient client) throws Exception {
    GetMethod method = new GetMethod(url);
    client.executeMethod(method);

    int code = method.getStatusCode();
    if (code != HttpStatus.SC_OK) {
      throw new IOException(code == HttpStatus.SC_BAD_REQUEST ?
                            method.getResponseBodyAsString() :
                            ("HTTP " + code + " (" + HttpStatus.getStatusText(code) + ") " + method.getStatusText()));
    }
    InputStream stream = method.getResponseBodyAsStream();
    Element root = new SAXBuilder(false).build(stream).getRootElement();
    Element channel = root.getChild("channel");
    if (channel != null) {
      List<Element> children = channel.getChildren("item");
      LOG.info("JIRA: " + children.size() + " issues found");
      return ContainerUtil.map(children, new Function<Element, Task>() {
        public Task fun(Element o) {
          return new JiraSoapTask(new JIRAIssueBean(myRepository.getUrl(), o, false), myRepository);
        }
      });
    }
    else {
      LOG.warn("JIRA channel not found");
    }
    return ContainerUtil.emptyList();
  }

  private HttpClient login() throws Exception {
    HttpClient client = myRepository.getHttpClient();
    client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    if (myJira4) {
      PostMethod postMethod = getLoginMethodFor4x();
      client.executeMethod(postMethod);
      if (checkLoginResult(postMethod)) {
        return client;
      }
    }
    // try 3.x protocol
    axisLogin();
    return client;
  }

  private void axisLogin() throws Exception {

    try {
      JiraSoapService soapService =
        new JiraSoapServiceServiceLocator().getJirasoapserviceV2(new URL(myRepository.getUrl() + "/rpc/soap/jirasoapservice-v2"));
      if (myRepository.isUseProxy()) {
        final List<KeyValue<String, String>> list = HttpConfigurable.getJvmPropertiesList(false, null);
        if (!list.isEmpty()) {
          for (KeyValue<String, String> value : list) {
            AxisProperties.setProperty(value.getKey(), value.getValue());
          }
        }
      }

      soapService.login(myRepository.getUsername(), myRepository.getPassword());
    }
    catch (RemoteException e) {
      String message = e.toString();
      int i = message.indexOf(": ");
      if (i > 0) {
        message = message.substring(i + 2);
      }
      throw new Exception(message, e);
    }
  }

  private boolean checkLoginResult(PostMethod postMethod) throws IOException {
    int statusCode = postMethod.getStatusCode();
    if (statusCode == HttpStatus.SC_NOT_FOUND) {
      myJira4 = false;
      return false;
    }
    if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
      throw new IOException("Can't login: " + statusCode + " (" + HttpStatus.getStatusText(statusCode) + ")");
    }
    if (statusCode == HttpStatus.SC_OK && new String(postMethod.getResponseBody(2000)).contains("\"loginSucceeded\":false")) {
      throw new IOException(JiraRepository.LOGIN_FAILED_CHECK_YOUR_PERMISSIONS);
    }
    return true;
  }

  private PostMethod getLoginMethodFor4x() {
    String url = myRepository.getUrl() + "/rest/gadget/1.0/login";
    PostMethod postMethod = new PostMethod(url);
    postMethod.addParameter("os_username", myRepository.getUsername());
    postMethod.addParameter("os_password", myRepository.getPassword());
    postMethod.addParameter("os_destination", "/success");
    return postMethod;
  }

  @Nullable
  @Override
  public Task findTask(String key) throws Exception {
    try {
      StringBuilder url = new StringBuilder(myRepository.getUrl());
      url.append("/si/jira.issueviews:issue-xml/");
      url.append(key).append('/').append(key).append(".xml");

      List<Task> tasks = processRSS(url.toString(), login());
      return tasks.isEmpty() ? null : tasks.get(0);
    }
    catch (Exception e) {
      LOG.warn("Cannot get issue " + key + ": " + e.getMessage());
      return null;
    }
  }

  @NotNull
  @Override
  public final ApiType getType() {
    return ApiType.SOAP;
  }

  @Override
  public void setTaskState(Task task, TaskState state) throws Exception {
    throw new Exception("Task state cannot be updated in JIRA versions prior 4.2.");
  }

  @Override
  public void updateTimeSpend(LocalTask task, String timeSpent, String comment) throws Exception {
    throw new Exception("Time spent cannot be updated in JIRA versions prior 4.2.");
=======
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.tasks.LocalTask;
import com.intellij.tasks.Task;
import com.intellij.tasks.TaskBundle;
import com.intellij.tasks.TaskState;
import com.intellij.tasks.impl.TaskUtil;
import com.intellij.tasks.jira.JiraRemoteApi;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Legacy SOAP connector restored due to IDEA-120595.
 *
 * @author Mikhail Golubev
 */
public class JiraSoapApi extends JiraRemoteApi {

  private static final Logger LOG = Logger.getInstance(JiraSoapApi.class);

  @NonNls private static final String RSS_SEARCH_PATH = "/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml";
  public static final String RSS_ISSUE_PATH = "/si/jira.issueviews:issue-xml/";

  public JiraSoapApi(@NotNull JiraRepository repository) {
    super(repository);
  }

  @NotNull
  @Override
  public List<Task> findTasks(@NotNull String query, int max) throws Exception {

    // Unfortunately, both SOAP and XML-RPC interfaces of JIRA don't allow fetching *all* tasks from server, but
    // only filtered by some search term (see http://stackoverflow.com/questions/764282/how-can-jira-soap-api-not-have-this-method).
    // JQL was added in SOAP only since JIRA 4.0 (see method JiraSoapService#getIssuesFromJqlSearch() at
    // https://docs.atlassian.com/software/jira/docs/api/rpc-jira-plugin/latest/index.html?com/atlassian/jira/rpc/soap/JiraSoapService.html)
    // So due to this limitation and the need to support these old versions of bug tracker (3.0, 4.2) we need the following ugly and hacky
    // solution with extracting issues from RSS feed.

    GetMethod method = new GetMethod(myRepository.getUrl() + RSS_SEARCH_PATH);
    method.setQueryString(new NameValuePair[] {
      new NameValuePair("tempMax", String.valueOf(max)),
      new NameValuePair("assignee", TaskUtil.encodeUrl(myRepository.getUsername())),
      new NameValuePair("reset", "true"),
      new NameValuePair("sorter/field", "updated"),
      new NameValuePair("sorter/order", "DESC"),
      new NameValuePair("pager/start", "0")
    });
    return processRSS(method);
  }

  private List<Task> processRSS(@NotNull GetMethod method) throws Exception {
    // Basic authorization should be enough
    int code = myRepository.getHttpClient().executeMethod(method);
    if (code != HttpStatus.SC_OK) {
      throw new Exception(TaskBundle.message("failure.http.error", code, method.getStatusText()));
    }
    Element root = new SAXBuilder(false).build(method.getResponseBodyAsStream()).getRootElement();
    Element channel = root.getChild("channel");
    if (channel != null) {
      List<Element> children = channel.getChildren("item");
      LOG.debug("Total issues in JIRA RSS feed: " + children.size());
      return ContainerUtil.map(children, new Function<Element, Task>() {
        public Task fun(Element element) {
          return new JiraSoapTask(element, myRepository);
        }
      });
    }
    else {
      LOG.warn("JIRA channel not found");
    }
    return ContainerUtil.emptyList();
  }

  @Nullable
  @Override
  public Task findTask(@NotNull String key) throws Exception {
    try {
      List<Task> tasks = processRSS(new GetMethod(myRepository.getUrl() + RSS_ISSUE_PATH + key + '/' + key + ".xml"));
      return tasks.isEmpty() ? null : tasks.get(0);
    }
    catch (Exception e) {
      LOG.warn("Cannot get issue " + key + ": " + e.getMessage());
      return null;
    }
  }

  @NotNull
  @Override
  public final ApiType getType() {
    return ApiType.SOAP;
  }

  @Override
  public void setTaskState(@NotNull Task task, @NotNull TaskState state) throws Exception {
    throw new Exception(TaskBundle.message("jira.failure.no.state.update"));
  }

  @Override
  public void updateTimeSpend(@NotNull LocalTask task, @NotNull String timeSpent, String comment) throws Exception {
    throw new Exception(TaskBundle.message("jira.failure.no.time.spent"));
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }
}
