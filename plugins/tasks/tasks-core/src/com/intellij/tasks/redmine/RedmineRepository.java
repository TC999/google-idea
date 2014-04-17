package com.intellij.tasks.redmine;

import com.google.gson.Gson;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.Task;
import com.intellij.tasks.TaskBundle;
import com.intellij.tasks.impl.gson.GsonUtil;
import com.intellij.tasks.impl.httpclient.NewBaseRepositoryImpl;
import com.intellij.tasks.redmine.model.RedmineIssue;
import com.intellij.tasks.redmine.model.RedmineProject;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.intellij.tasks.impl.httpclient.ResponseUtil.GsonSingleObjectDeserializer;
import static com.intellij.tasks.redmine.model.RedmineResponseWrapper.*;

/**
 * @author Mikhail Golubev
 * @author Dennis.Ushakov
 */
@Tag("Redmine")
public class RedmineRepository extends NewBaseRepositoryImpl {
  private static final Logger LOG = Logger.getInstance(RedmineRepository.class);
  private static final Gson GSON = GsonUtil.createDefaultBuilder().create();

  private static final Pattern ID_PATTERN = Pattern.compile("\\d+");

  public static final RedmineProject UNSPECIFIED_PROJECT = new RedmineProject() {
    @NotNull
    @Override
    public String getName() {
      return "-- from all projects --";
    }

    @Override
    public int getId() {
      return -1;
    }
  };

  private String myAPIKey = "";
  private RedmineProject myCurrentProject;
  private List<RedmineProject> myProjects = null;

  /**
   * Serialization constructor
   */
  @SuppressWarnings({"UnusedDeclaration"})
  public RedmineRepository() {
    // empty
  }

  /**
   * Normal instantiation constructor
   */
  public RedmineRepository(RedmineRepositoryType type) {
    super(type);
    setUseHttpAuthentication(true);
  }


  /**
   * Cloning constructor
   */
  public RedmineRepository(RedmineRepository other) {
    super(other);
    setAPIKey(other.myAPIKey);
    setCurrentProject(other.getCurrentProject());
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    if (!(o instanceof RedmineRepository)) return false;
    RedmineRepository that = (RedmineRepository)o;
    if (!Comparing.equal(getAPIKey(), that.getAPIKey())) return false;
    if (!Comparing.equal(getCurrentProject(), that.getCurrentProject())) return false;
    return true;
  }

  @NotNull
  @Override
  public RedmineRepository clone() {
    return new RedmineRepository(this);
  }

  @Override
  public void testConnection() throws Exception {
    // Strangely, but Redmine doesn't return 401 or 403 error if client sent wrong credentials and instead
    // merely returns empty array of issues with status code of 200. This means that we should attempt to fetch
    // something more specific than issues to test connection and configuration.
    HttpResponse response = getHttpClient().execute(new HttpGet(getRestApiUrl("users", "current.json")));
    //TaskUtil.prettyFormatResponseToLog(LOG, response);
    int code = response.getStatusLine().getStatusCode();
    if (code != HttpStatus.SC_OK) {
      if (code == HttpStatus.SC_UNAUTHORIZED) {
        throw new Exception(TaskBundle.message("failure.login"));
      }
      throw new Exception(TaskBundle.message("failure.http.error", code, response.getStatusLine().getReasonPhrase()));
    }
  }

  @Override
  public Task[] getIssues(@Nullable String query, int offset, int limit, boolean withClosed) throws Exception {
    List<RedmineIssue> issues = fetchIssues(query, offset, limit, withClosed);
    return ContainerUtil.map2Array(issues, RedmineTask.class, new Function<RedmineIssue, RedmineTask>() {
      @Override
      public RedmineTask fun(RedmineIssue issue) {
        return new RedmineTask(RedmineRepository.this, issue);
      }
    });
  }

  public List<RedmineIssue> fetchIssues(String query, int offset, int limit, boolean withClosed) throws Exception {
    ensureProjectsDiscovered();
    URIBuilder builder = new URIBuilder(getRestApiUrl("issues.json"))
      .addParameter("offset", String.valueOf(offset))
      .addParameter("limit", String.valueOf(limit))
      .addParameter("status_id", withClosed ? "*" : "open")
      .addParameter("assigned_to_id", "me");

    // Legacy API, can't find proper documentation
    //if (StringUtil.isNotEmpty(query)) {
    //  builder.addParameter("fields[]", "subject").addParameter("operators[subject]", "~").addParameter("values[subject][]", query);
    //}
    // If project was not chosen, all available issues still fetched. Such behavior may seems strange to user.
    if (myCurrentProject != null && myCurrentProject != UNSPECIFIED_PROJECT) {
      builder.addParameter("project_id", String.valueOf(myCurrentProject.getId()));
    }
    if (isUseApiKeyAuthentication()) {
      builder.addParameter("key", myAPIKey);
    }
    HttpClient client = getHttpClient();
    HttpGet method = new HttpGet(builder.toString());
    return client.execute(method, new GsonSingleObjectDeserializer<IssuesWrapper>(GSON, IssuesWrapper.class)).getIssues();
  }

  public List<RedmineProject> fetchProjects() throws Exception {
    URIBuilder builder = new URIBuilder(getRestApiUrl("projects.json"));
    if (isUseApiKeyAuthentication()) {
      builder.addParameter("key", myAPIKey);
    }
    HttpClient client = getHttpClient();
    HttpGet method = new HttpGet(builder.toString());
    ProjectsWrapper wrapper = client.execute(method, new GsonSingleObjectDeserializer<ProjectsWrapper>(GSON, ProjectsWrapper.class));
    myProjects = wrapper.getProjects();
    return Collections.unmodifiableList(myProjects);
  }

  @Nullable
  @Override
  public Task findTask(@NotNull String id) throws Exception {
    ensureProjectsDiscovered();
    HttpGet method = new HttpGet(getRestApiUrl("issues", id + ".json"));
    IssueWrapper wrapper = getHttpClient().execute(method, new GsonSingleObjectDeserializer<IssueWrapper>(GSON, IssueWrapper.class));
    if (wrapper == null) {
      return null;
    }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    final String summary = element.getChildText("subject");
    if (summary == null) {
      return null;
    }
    final Element status = element.getChild("status");
    final boolean isClosed = status == null || "Closed".equals(status.getAttributeValue("name"));
    final String description = element.getChildText("description");
    final Ref<Date> updated = new Ref<Date>();
    final Ref<Date> created = new Ref<Date>();
    try {
      updated.set(parseDate(element, "updated_on"));
      created.set(parseDate(element, "created_on"));
    } catch (ParseException e) {
      LOG.warn(e);
    }

    return new Task() {
      @Override
      public boolean isIssue() {
        return true;
      }

      @Override
      public String getIssueUrl() {
        final String id = getRealId(getId());
        return id != null ? getUrl() + "/issues/" + id : null;
      }

      @NotNull
      @Override
      public String getId() {
        return myProjectId + "-" + id;
      }

      @NotNull
      @Override
      public String getSummary() {
        return summary;
      }

      public String getDescription() {
        return description;
      }

      @NotNull
      @Override
      public Comment[] getComments() {
        return new Comment[0];
      }

      @NotNull
      @Override
      public Icon getIcon() {
        return TasksIcons.Redmine;
      }

      @NotNull
      @Override
      public TaskType getType() {
        return TaskType.BUG;
      }

      @Override
      public Date getUpdated() {
        return updated.get();
      }

      @Override
      public Date getCreated() {
        return created.get();
      }

      @Override
      public boolean isClosed() {
        return isClosed;
      }

      @Override
      public TaskRepository getRepository() {
        return RedmineRepository.this;
      }

      @Override
      public String getPresentableName() {
        return getId() + ": " + getSummary();
      }
    };
  }

  @Nullable
  private static Date parseDate(Element element, String name) throws ParseException {
    final String date = element.getChildText(name);
    if (date.matches(".*\\+\\d\\d:\\d\\d")) {
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss", Locale.US);
      final int timeZoneIndex = date.length() - 6;
      format.setTimeZone(TimeZone.getTimeZone("GMT" + date.substring(timeZoneIndex)));
      return format.parse(date.substring(0, timeZoneIndex));
    }
    // Ad-hoc fix for IDEA-110012
    Date parsed;
    try {
      parsed = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US).parse(date);
    }
    catch (ParseException e) {
      parsed = TaskUtil.parseDate(date);
    }
    return parsed;
  }

  @Override
  public boolean isConfigured() {
    return super.isConfigured() && !StringUtil.isEmpty(myProjectId);
  }

  private List<Element> getIssues(@Nullable String query, int max) throws Exception {
    String url = "/projects/" + myProjectId + "/issues.xml?";
    final boolean hasKey = !StringUtil.isEmpty(myAPIKey) && !isUseHttpAuthentication();
    if (hasKey) {
      url +="key=" + myAPIKey;
    }
    if (hasKey) url += "&";
    // getting only open id's
    url += encodeUrl("fields[]") + "=status_id&"  + 
           encodeUrl("operators[status_id]") + "=o&" +
           encodeUrl("values[status_id][]") + "=1";
    final boolean hasQuery = !StringUtil.isEmpty(query);
    if (hasQuery) {
      url += "&" + encodeUrl("fields[]") + "=subject&" +
             encodeUrl("operators[subject]") + "=" + encodeUrl("~") + "&" + 
             encodeUrl("values[subject][]") + "=" + encodeUrl(query);
    }
    if (max >= 0) {
      url += "&per_page=" + encodeUrl(String.valueOf(max));
    }
    HttpMethod method = doREST(url, false);
    final String response = method.getResponseBodyAsString();
    final Reader stream = new StringReader(response);
    final InputSource source = new InputSource(stream);
    source.setEncoding("UTF-8");
    Element element;
    try {
      element = new SAXBuilder(false).build(source).getRootElement();
    } catch (Throwable t) {
      LOG.error("Error fetching issues for: " + url + ", HTTP status code: " + method.getStatusCode(), t, response);
      throw new Exception("Error fetching issues for: " + url + ", HTTP status code: " + method.getStatusCode() +
                          "\n" + response);
    }

    if (!"issues".equals(element.getName())) {
      LOG.warn("Error fetching issues for: " + url + ", HTTP status code: " + method.getStatusCode());
      throw new Exception("Error fetching issues for: " + url + ", HTTP status code: " + method.getStatusCode() +
                          "\n" + element.getText());
    }

    return element.getChildren("issue");
  }

  private HttpMethod doREST(String request, boolean post) throws Exception {
    final HttpClient client = getHttpClient();
    client.getParams().setContentCharset("UTF-8");
    String uri = getUrl() + request;
    HttpMethod method = post ? new PostMethod(uri) : new GetMethod(uri);
    configureHttpMethod(method);
    client.executeMethod(method);
    return method;
  }

  @Nullable
  @Override
  public Task findTask(String id) throws Exception {
    final String realId = getRealId(id);
    if (realId == null) return null;
    HttpMethod method = doREST("/issues/" + realId + ".xml", false);
    InputStream stream = method.getResponseBodyAsStream();
    Element element = new SAXBuilder(false).build(stream).getRootElement();
    return element.getName().equals("issue") ? createIssue(element) : null;
  }

  @Override
  public BaseRepository clone() {
    return new RedmineRepository(this);
  }

  @Nullable
  private String getRealId(String id) {
    final String start = myProjectId + "-";
    return id.startsWith(start) ? id.substring(start.length()) : null;
  }

  @Nullable
  public String extractId(String taskName) {
    Matcher matcher = myPattern.matcher(taskName);
    return matcher.find() ? matcher.group(1) : null;
=======
    return new RedmineTask(this, wrapper.getIssue());
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  public String getAPIKey() {
    return myAPIKey;
  }

  public void setAPIKey(String APIKey) {
    myAPIKey = APIKey;
  }

  private boolean isUseApiKeyAuthentication() {
    return !StringUtil.isEmptyOrSpaces(myAPIKey) && !isUseHttpAuthentication();
  }

  @Override
  public String getPresentableName() {
    String name = super.getPresentableName();
    if (myCurrentProject != null && myCurrentProject != UNSPECIFIED_PROJECT) {
      name += "/projects/" + myCurrentProject.getIdentifier();
    }
    return name;
  }

  @Override
  public boolean isConfigured() {
    if (!super.isConfigured() || StringUtil.isEmpty(myUsername)) return false;
    if (isUseHttpAuthentication()) {
      return StringUtil.isNotEmpty(myPassword);
    }
    return !StringUtil.isEmptyOrSpaces(myAPIKey);
  }

  @Nullable
  @Override
  public String extractId(@NotNull String taskName) {
    return ID_PATTERN.matcher(taskName).matches()? taskName : null;
  }

  @Override
  protected int getFeatures() {
    return super.getFeatures() & ~NATIVE_SEARCH | BASIC_HTTP_AUTHORIZATION;
  }

  @Nullable
  public RedmineProject getCurrentProject() {
    return myCurrentProject;
  }

  public void setCurrentProject(@Nullable RedmineProject project) {
    myCurrentProject = project != null && project.getId() == -1 ? UNSPECIFIED_PROJECT : project;
  }

  @NotNull
  public List<RedmineProject> getProjects() {
    try {
      ensureProjectsDiscovered();
    }
    catch (Exception ignored) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(myProjects);
  }

  private void ensureProjectsDiscovered() throws Exception {
    if (myProjects == null) {
      fetchProjects();
    }
  }

  @TestOnly
  @Transient
  public void setProjects(@NotNull List<RedmineProject> projects) {
    myProjects = projects;
  }
}
