package edu.oregonstate.templates

import edu.oregonstate.util.CallbackModel

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 7/12/16.
 */
class FragmentTemplate extends BaseTemplate {

    FragmentTemplate(String permission, String contextClass, String contextMethodSignature,
                     String targetText, String requestCodeName, Integer requestCodeValue, CallbackModel model) {
        super(permission, contextClass, contextMethodSignature, targetText, requestCodeName, requestCodeValue, model)
    }

    @Override
    protected String toCheckBlockBefore() {
        def block =
        """if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (android.support.v4.app.ActivityCompat.checkSelfPermission(getActivity(), ${qualifiedPermissionName})
            != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[] { ${qualifiedPermissionName} }, ${requestCodeName});
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
        def block =
        """@Override
        public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions,
            @android.support.annotation.NonNull int[] grantResults) {
            if (requestCode == ${requestCodeName}) {
                if (grantResults.length == 1 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    ${targetText}
                } else {
                    android.widget.Toast.makeText(this.getContext(), "${permissionName} Permission Denied", android.widget.Toast.LENGTH_LONG).show();
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }"""
        return block
    }

    @Override
    protected String toCallbackIfBranch() {
        def block =
        """ else if (requestCode == ${requestCodeName}) {
                if (grantResults.length == 1 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    ${targetText}
                } else {
                    android.widget.Toast.makeText(this.getContext(), "${permissionName} Permission Denied", android.widget.Toast.LENGTH_LONG).show();
                }
            }"""
        return block
    }

    @Override
    protected String toCallbackSwitchBranch() {
        def block =
        """case ${requestCodeName}:
            if (grantResults.length == 1 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ${targetText}
            } else {
                android.widget.Toast.makeText(this.getContext(), "${permissionName} Permission Denied", android.widget.Toast.LENGTH_LONG).show();
            }"""
        return block
    }
}
