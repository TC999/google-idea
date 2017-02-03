package edu.oregonstate.templates

import edu.oregonstate.util.CallbackModel

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 8/13/16.
 */
class DefaultTemplate extends BaseTemplate {

    DefaultTemplate(String permission, String contextClass, String contextMethodSignature,
                    String targetText, String requestCodeName, Integer requestCodeValue, CallbackModel model) {
        super(permission, contextClass, contextMethodSignature, targetText, requestCodeName, requestCodeValue, model)
    }

    @Override
    protected String toCheckBlockBefore() {
        def block =
        """if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (android.support.v4.app.ActivityCompat.checkSelfPermission(context, ${qualifiedPermissionName})
            != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // permission not granted, consider calling ActivityCompat.requestPermissions()
        } else {"""
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
        // default target does not have guaranteed access to a running UI thread
        return """"""
    }

    @Override
    protected String toCallbackIfBranch() {
        // default target does not have guaranteed access to a running UI thread
        return """"""
    }

    @Override
    protected String toCallbackSwitchBranch() {
        // default target does not have guaranteed access to a running UI thread
        return """"""
    }
}
