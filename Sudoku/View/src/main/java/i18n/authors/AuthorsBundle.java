package i18n.authors;

import java.util.ListResourceBundle;

public class AuthorsBundle extends ListResourceBundle {

    private final Object[][] resources = {{"authors_university", "Politechnika \u0141\u00F3dzka"}, {"authors_country", "Polska"}};

    @Override
    protected Object[][] getContents() {
        return resources;
    }
}
