package edu.oregonstate.templates

import edu.oregonstate.util.CallbackModel

/**
 * @author Jacob Lewis <lewisj3@oregonstate.edu> Created on on 7/21/16.
 */
class ViewTemplate extends BaseTemplate {

    ViewTemplate(String permission, String contextClass, String contextMethodSignature,
                 String targetText, String requestCodeName, Integer requestCodeValue, CallbackModel model) {
        super(permission, contextClass, contextMethodSignature, targetText, requestCodeName, requestCodeValue, model)
    }

    @Override
    protected String toCheckBlockBefore() {
        def block =
                """if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (android.support.v4.app.ActivityCompat.checkSelfPermission(this.getContext(), ${qualifiedPermissionName})
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
        // android.view.View does not have guaranteed access to a running UI thread
        return """"""
    }

    @Override
    protected String toCallbackIfBranch() {
        // android.view.View does not have guaranteed access to a running UI thread
        return """"""
    }

    @Override
    protected String toCallbackSwitchBranch() {
        // android.view.View does not have guaranteed access to a running UI thread
        return """"""
    }

}
