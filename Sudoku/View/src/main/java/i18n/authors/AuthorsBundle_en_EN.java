package i18n.authors;
import java.util.ListResourceBundle;

public class AuthorsBundle_en_EN extends ListResourceBundle {

    private final Object[][] resources = {{"authors_university", "Technical University of Lodz"}, {"authors_country", "Poland"}};

    @Override
    protected Object[][] getContents() {
        return resources;
    }
}
