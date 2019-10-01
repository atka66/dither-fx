package hu.atka.ditherfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import hu.atka.ditherfx.resources.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

public class MainController implements Initializable {

	@FXML
	private Canvas canvas;

	private GraphicsContext gc;
	private Image image;
	private PixelWriter pw;
	private boolean isDithered;

	@FXML
	private void onClick(MouseEvent event) {
		isDithered = !isDithered;
		render();
	}

	public void initialize(URL location, ResourceBundle resources) {
		canvas.setWidth(Settings.CANVAS_WIDTH);
		canvas.setHeight(Settings.CANVAS_HEIGHT);
		gc = canvas.getGraphicsContext2D();
		image = Settings.IMAGE;
		pw = gc.getPixelWriter();
		isDithered = false;
		render();
	}

	private void render() {
		clearCanvas();
		if (isDithered) {
			System.out.println("Rendering dithered image...");
			clearImageBackground();
			renderFloydSteinbergDitheredImage();
		} else {
			System.out.println("Rendering raw image...");
			renderRawImage();
		}
	}

	private void clearCanvas() {
		gc.setFill(Settings.COLOR_FILL_BOARD_CLEAR);
		gc.fillRect(0, 0, Settings.CANVAS_WIDTH, Settings.CANVAS_HEIGHT);
	}

	private void clearImageBackground() {
		int imageWidth = image.widthProperty().intValue();
		int imageHeight = image.heightProperty().intValue();
		gc.setFill(Settings.COLOR_PIXEL_EMPTY);
		gc.fillRect(0, 0, imageWidth, imageHeight);
	}

	private void renderRawImage() {
		gc.drawImage(Settings.IMAGE, 0, 0);
	}

	private void renderOrderedDitheredImage() {
		int imageWidth = image.widthProperty().intValue();
		int imageHeight = image.heightProperty().intValue();

		PixelReader pr = Settings.IMAGE.getPixelReader();
		for (int i = 0; i < imageWidth / 2; i++) {
			for (int j = 0; j < imageHeight / 2; j++) {
				double brightness = pr.getColor(i * 2, j * 2).getBrightness();
				if (brightness < 0.8) {
					pw.setColor((i * 2) + 1, (j * 2), Settings.COLOR_PIXEL_FILL);
					if (brightness < 0.6) {
						pw.setColor((i * 2), (j * 2) + 1, Settings.COLOR_PIXEL_FILL);
						if (brightness < 0.4) {
							pw.setColor((i * 2), (j * 2), Settings.COLOR_PIXEL_FILL);
							if (brightness < 0.2) {
								pw.setColor((i * 2) + 1, (j * 2) + 1, Settings.COLOR_PIXEL_FILL);
							}
						}
					}
				}
			}
		}
	}

	private void renderFloydSteinbergDitheredImage() {
		int imageWidth = image.widthProperty().intValue();
		int imageHeight = image.heightProperty().intValue();

		PixelReader pr = Settings.IMAGE.getPixelReader();
		double[][] errorMap = new double[imageWidth][imageHeight];
		for (int i = 1; i < imageWidth - 1; i++) {
			for (int j = 1; j < imageHeight - 1; j++) {
				double brightness = pr.getColor(i, j).getBrightness() + errorMap[i][j];
				double error;
				if (brightness > Settings.FLOYD_STEINBERG_THRESHOLD) {
					error = 1.0 - brightness;
				} else {
					pw.setColor(i, j, Settings.COLOR_PIXEL_FILL);
					error = -brightness;
				}
				errorMap[i + 1][j] -= 7 * error / 16;
				errorMap[i + 1][j + 1] -= 1 * error / 16;
				errorMap[i][j + 1] -= 5 * error / 16;
				errorMap[i - 1][j + 1] -= 3 * error / 16;
			}
		}
	}
}
