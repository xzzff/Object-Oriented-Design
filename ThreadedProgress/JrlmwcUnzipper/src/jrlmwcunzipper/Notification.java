/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrlmwcunzipper;

/**
 *
 * @author joe
 */
@FunctionalInterface
public interface Notification
{
    public void handle(double percentComplete, String extractPath, Status status);
}
