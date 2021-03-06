package org.amityregion5.tictactical.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import org.amityregion5.tictactical.TicTactical;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(720, 480);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new TicTactical();
        }
}