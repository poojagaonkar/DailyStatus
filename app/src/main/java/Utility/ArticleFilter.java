package Utility;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;


public class ArticleFilter implements Predicate<StatusResponse>
{
    private final Pattern pattern;

    public ArticleFilter(final String regex)
    {
        pattern = Pattern.compile(regex);
    }

    @Override
    public boolean apply(final StatusResponse input)
    {
    	
        return pattern.matcher(input.getSlot()).find();
    }
}