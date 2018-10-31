package org.revocen.cavmp.event;


import org.revocen.cavmp.model.CavmpError;

import java.util.Map;

/**
 * Created by Nikolay Demyankov on 28.08.15.
 * <p/>
 * Interface describes plugin specific events.
 */
public interface IPluginEvent {

    /**
     * String identifier of the event.
     * Used for dispatching same event in JavaScript.
     */
    String name();

    /**
     * Error information, that is attached to the event
     *
     * @see ChcpError
     */
    CavmpError error();

    /**
     * Additional user information, attached to the event
     *
     * @return map with additional event data
     */
    Map<String, Object> data();

}
