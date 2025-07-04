/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.exception;

/**
 *
 * @author Hp
 */
public class MandatoryFieldException extends RuntimeException{
    
    public MandatoryFieldException(String message){
        super(message);
    }
}
