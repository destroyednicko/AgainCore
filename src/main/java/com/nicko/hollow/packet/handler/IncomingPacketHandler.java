package com.nicko.hollow.packet.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifica o método e tipo de pacote atribuído ao método.
 * Os únicos tipos de parâmetro válidos para o método que é 'declarado'
 * por essa anotação é 'packet' e 'string'.
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IncomingPacketHandler {

}
