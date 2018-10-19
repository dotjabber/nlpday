package pl.nlp.day.commons;

import com.google.common.base.Function;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 10/11/12
 * Time: 11:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TagRemovalFilter implements Function<String, String> {


    public String apply(@Nullable String input) {

        if (StringUtils.isBlank(input)) {
            return input;
        }
        return input.replaceAll("\\<.*?\\>", " ");
    }
}
