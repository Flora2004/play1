package client.strategy;

import client.Tank;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-18
 * Time: 14:30
 */
public interface FireStrategy extends Serializable {
    void fire(Tank t);
}
