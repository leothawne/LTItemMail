package br.net.gmj.nobookie.LTItemMail.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LTCommandInfo {
    String name() default "";
    String description() default "";
    String aliases() default "";
    String permission() default "";
    String usage() default "/<command>";
}
