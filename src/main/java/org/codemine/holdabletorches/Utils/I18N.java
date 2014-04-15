package org.codemine.holdabletorches.Utils;

import org.codemine.holdabletorches.Torches;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Name: I18N.java Created: 15 April 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class I18N {

    private final static Locale locale = Torches.locale;

    @NonNls
    private static final ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);

    public static String STRING
      (@PropertyKey(resourceBundle = "MessagesBundle")
       String key, Object... params)
    {

        String value = bundle.getString(key);
        if(params.length > 0) return MessageFormat.format(value, params);
        return value;
    }
}
