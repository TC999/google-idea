package edu.oregonstate.templates

import edu.oregonstate.util.CallbackModel

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 7/12/16.
 */
class ApplicationTemplate extends BaseTemplate {

    ApplicationTemplate(String permission, String contextClass, String contextMethodSignature,
                        String targetText, String requestCodeName, Integer requestCodeValue, CallbackModel model) {
        super(permission, contextClass, contextMethodSignature, targetText, requestCodeName, requestCodeValue, model)
    }

    @Override
    protected String toCheckBlockBefore() {
        def block =
        """if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (android.support.v4.app.ActivityCompat.checkSelfPermission(${contextClassName}.this, ${qualifiedPermissionName})
            == android.content.pm.PackageManager.PERMISSION_GRANTED) {
        """
        return block
    }

    @Override
    protected String toCheckBlockAfter() {
        def block =
        """
        }
        } else {
        """
        return block
    }

    @Override
    protected String toCheckBlockElse() {
        def block =
        """
        }
        """
        return block
    }

    @Override
    protected String toCallbackMethod() {
        // android.app.Application does not have guaranteed access to a running UI thread
        return """"""
    }

    @Override
    protected String toCallbackIfBranch() {
        // android.app.Application does not have guaranteed access to a running UI thread
        return """"""
    }

    @Override
    protected String toCallbackSwitchBranch() {
        // android.app.Application does not have guaranteed access to a running UI thread
        return """"""
    }
}
