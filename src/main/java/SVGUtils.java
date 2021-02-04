import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * A class that handles operations on SVG files representing card images, each SVG file has
 * to be converted to an Image object before it gets inserted into an ImageView
 */
public class SVGUtils {
    public static Image clubsSymbol = getSVGSuitSymbol(Suit.CLUBS);
    public static Image diamondsSymbol = getSVGSuitSymbol(Suit.DIAMONDS);
    public static Image spadesSymbol = getSVGSuitSymbol(Suit.SPADES);
    public static Image heartsSymbol = getSVGSuitSymbol(Suit.HEARTS);

    /**
     * @param suit
     * @return An Image object representing the requested suit
     */
    public static Image getSymbol(Suit suit) {
        return switch (suit) {
            case SPADES -> spadesSymbol;
            case CLUBS -> clubsSymbol;
            case HEARTS -> heartsSymbol;
            case DIAMONDS -> diamondsSymbol;
        };
    }

    /**
     * @param path A String containing a path to the svg file we want to convert to the Image object
     * @return calls another - getImage(path, transcoder) in result returning an Image object representing the svg file
     */
    public static Image getImageFromSVG(String path) {

        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, 100F);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, 140F);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, new Color(0.72f, 0.71f, 0.23f));

        return getImage(path, transcoder);
    }

    /**
     * @param suit A card's suit object.getS is called returning a string containing the card's suit name
     * @param denomination A card's denomination object.getS is called returning a string containing the card's
     *                    denomination name
     * @return A path to the card's SVG resource file, since all the card's SVGs are named in the same convention:
     *          "/imagesSVG/<card's_denomination_shortcut><card's_suit_shortcut>.svg"
     */
    public static String getSVGCardResourcePath(Suit suit, Denomination denomination) {
        return "/imagesSVG/" +
                denomination.getS() +
                suit.getS() +
                ".svg";
    }

    /**
     * @param suit
     * @return an Image object containing card's suit symbol
     */
    public static Image getSVGSuitSymbol(Suit suit) {
        String path = getSVGCardResourcePath(suit, Denomination.TWO);

        Rectangle aoi = new Rectangle(-37, 62, 75, 75);

        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

        transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float) aoi.width);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float) aoi.height);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_AOI, aoi);


        return getImage(path, transcoder);
    }

    private static Image getImage(String path,  BufferedImageTranscoder transcoder) {
        Image image = null;
        try (InputStream file = SVGUtils.class.getResourceAsStream(path)) {
            TranscoderInput transIn = new TranscoderInput(file);
            try {
                transcoder.transcode(transIn, null);
                image = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
            } catch (TranscoderException ex) {
                ex.printStackTrace();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return image;
    }
}
