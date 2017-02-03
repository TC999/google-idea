package edu.oregonstate.util

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 7/18/16.
 */
enum CallbackModel {
    NONE,           // no callback allowed in this context
    NEW_METHOD,     // a new callback method is required
    IF_BLOCK,       // a new if branch in an existing callback method is required
    SWITCH_BLOCK    // a new switch branch in an existing callback method is required
}