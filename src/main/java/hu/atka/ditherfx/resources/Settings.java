package hu.atka.ditherfx.resources;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Settings {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int CANVAS_WIDTH = WINDOW_WIDTH;
	public static final int CANVAS_HEIGHT = WINDOW_HEIGHT;
	public static final Image IMAGE = new Image(Settings.class.getResourceAsStream("/image/image2.jpg"));

	public static final Color COLOR_FILL_BOARD_CLEAR = Color.SILVER;
	public static final Color COLOR_PIXEL_EMPTY = Color.WHITE;
	public static final Color COLOR_PIXEL_FILL = Color.BLACK;

	public static final double FLOYD_STEINBERG_THRESHOLD = 0.5;
}
