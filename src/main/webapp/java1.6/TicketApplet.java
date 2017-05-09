import java.applet.Applet;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import netscape.javascript.JSObject;

import javax.print.PrintService;
import javax.print.attribute.PrintRequestAttributeSet;

import com.sun.java_cup.internal.parse_action;

public class TicketApplet extends Applet implements Printable {

	private Graphics g;

	Rectangle2D rect = null;

	private FontMetrics fm;

	private int height = 0;
	private int prevyAxis = 10;
	int appletWidth = 0;
	public double abc = 0.0;

	public static enum GameNameEnum {
		Lotto, Zimlotto, Fastlotto, Fortune, Card12, CARD16, Keno, Zerotonine, TwelveByTwentyFour
	}

	public enum Alignment {
		CENTERED, LEFTMARGIN, RIGHTMARGIN, LEFTALONGRIGHT
	}

	private GameNameEnum gameNameEnum;
	private Alignment alignment;

	public void setXY() {
		height = 0;
		prevyAxis = 10;
	}

	public static String[] fortuneData = { "Aries", "Taurus", "Gemini",
			"Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius",
			"Capricorn", "Aquarius", "Pisces" };

	public static String[] card12Data = { "Ace_Spade", "Ace_Heart",
			"Ace_Diamond", "Ace_Club", "King_Spade", "King_Heart",
			"King_Diamond", "King_Club", "Queen_Spade", "Queen_Heart",
			"Queen_Diamond", "Queen_Club" };

	public static String[] card16Data = { "Ace_Spade", "Ace_Heart",
			"Ace_Diamond", "Ace_Club", "King_Spade", "King_Heart",
			"King_Diamond", "King_Club", "Queen_Spade", "Queen_Heart",
			"Queen_Diamond", "Queen_Club", "Jack_Spade", "Jack_Heart",
			"Jack_Diamond", "Jack_Club" };

	public static String[] zeroToNineData = { "Zero(0)", "One(1)", "Two(2)",
			"Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)",
			"Nine(9)" };

	public TicketApplet() {
		img = null;
	}

	public void destroy() {
	}

	public void init() {
		Linear linear = new Linear();
		app = getAppletContext().getApplet("TicketApp");
		linear.barcode = app.getParameter("data");
		BufferedImage bufferedimage = new BufferedImage(100, 100, 13);
		Graphics2D graphics2d = bufferedimage.createGraphics();
		linear.paint(graphics2d);
		linear.invalidate();
		graphics2d.dispose();
		BufferedImage bufferedimage1 = new BufferedImage(
				linear.getSize().width, linear.getSize().height, 13);
		Graphics2D graphics2d1 = bufferedimage1.createGraphics();
		linear.paint(graphics2d1);
		img = bufferedimage1;
		try {

			PrinterJob printJob = PrinterJob.getPrinterJob();
			PageFormat pageFormat = printJob.defaultPage();
			Paper paper = pageFormat.getPaper();
			paper
					.setImageableArea(0.0, 0.0, Double.MAX_VALUE,
							Double.MAX_VALUE);
			pageFormat.setPaper(paper);
			printJob.setPrintable(this, pageFormat);

			PrintService printer = printJob.getPrintService();

			/*
			 * System.out .println("***********Printer value************" +
			 * printer);
			 */
			if (printer != null) { // See if there is a printer

				try {
					/* System.out.println(printJob); */
					printJob.print(); // ...then print
					/* System.out.println("b4 printer exception........."); */
				} catch (PrinterException pe) {
					/*
					 * System.out .println("*********111********************In
					 * Catch if printer not null");
					 */
					notifyRPOS();
				}
			} else {
				/*
				 * System.out .println("**************222*********************In
				 * Catch if printer null");
				 */
				notifyRPOS();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyRPOS() {

		double totAmt = 0.0;
		String drawAmt = getParameter("drawAmtStr");
		if (drawAmt != null) {
			String[] amt = drawAmt.split(",");

			for (int i = 0; i < amt.length; i++) {
				try {
					totAmt = totAmt
							+ Double.parseDouble(amt[i].replace("NA", "0.0"));
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
			}
		}
		Object[] obj = new Object[2];
		obj[0] = app.getParameter("data");
		obj[1] = totAmt;
		JSObject jsOb = (JSObject) JSObject.getWindow(this);
		jsOb.call("cancelTktAuto", obj);

	}

	public void start() {
	}

	public void stop() {
	}

	private void setGraphics(Graphics g) {
		this.g = g;
	}

	public void paint(Graphics g) {
		setXY();
		setGraphics(g);

		String mode = this.getParameter("mode");

		if (mode.equals("Buy")) {
			prepareTicket();
		} else if (mode.equals("PWT")) {
			preparePWT();
		}
	}

	private void prepareTicket() {
		java.awt.Image newImage = Toolkit.getDefaultToolkit().createImage(
				img.getSource());
		Map<TextAttribute, Object> textAttributes = new HashMap<TextAttribute, Object>();
		String gameNum;
		String betAmountMultiple = this.getParameter("betAmountMultiple");
		String ticketNumber = this.getParameter("ticketNumber");
		String isQuickPick = this.getParameter("isQuickPick");
		String noOfDraws = this.getParameter("noOfDraws");
		String pickedNumbers = this.getParameter("pickedNumbers");
		String drawDateTime = this.getParameter("drawDateTime");
		String totalPurchaseAmt = this.getParameter("totalPurchaseAmt");
		String saleStatus = this.getParameter("saleStatus");
		String purchaseTime = this.getParameter("purchaseTime");
		String reprintCount = this.getParameter("reprintCount");
		String currSymbol = this.getParameter("currSymbol");
		String orgName = this.getParameter("orgName");
		String gameName = this.getParameter("gameName");
		String gameDispName = this.getParameter("gameDispName");
		String playType = this.getParameter("playType");
		String totalQuantity = this.getParameter("totalQuantity");
		// System.out.println("DATE CTR:" + this.getParameter("ctr"));
		String[] purchaseTimeArr = purchaseTime.split(" ");
		String[] drawDateTimeArr = drawDateTime.split(",");
		String[] betAmountMultArr = (betAmountMultiple != null) ? betAmountMultiple
				.split(",")
				: null;
		String[] kenoNums = pickedNumbers.split(",");
		String[] pickedNumbersArr = null;
		if (gameName.equals("Fortune") || gameName.equals("Zerotonine")
				|| gameName.equals("CARD16") || gameName.equals("Card12")) {
			pickedNumbersArr = pickedNumbers.split(",");
		} else if (gameName.equals("Lotto") || gameName.equals("Zimlotto")
				|| gameName.equals("Fastlotto")) {
			pickedNumbersArr = pickedNumbers.split(";");
		}

		String data = app.getParameter("data");
		int betAmtVal = 0;
		int pickedNumVal = 0;

		double unitPrice;
		unitPrice = (Double.parseDouble(totalPurchaseAmt) / (Double
				.parseDouble(noOfDraws) * Double.parseDouble(totalQuantity)));
		String str;
		appletWidth = getWidth();

		// System.out.println("appletWidth: " + appletWidth);
		// System.out.println("appletHeight: " + getHeight());
		String[] symbolArr = getWinningArray(GameNameEnum.valueOf(gameName));

		// setting beginning astericks
		Font font = new Font("Arial", Font.PLAIN, 10);

		drawFormattedString("**********************************", font, 0, 0,
				alignment.CENTERED);

		// setting organization name
		textAttributes.put(TextAttribute.WEIGHT, 300f);
		textAttributes.put(TextAttribute.FAMILY, "Letter Gothic Std");
		textAttributes.put(TextAttribute.SIZE, 15f);
		font = this.getFont().deriveFont(textAttributes);
		drawFormattedString(orgName, font, 0, 0, alignment.CENTERED);
		textAttributes.clear();

		textAttributes.put(TextAttribute.WEIGHT, 800f);
		textAttributes.put(TextAttribute.FAMILY, "Letter Gothic Std");
		textAttributes.put(TextAttribute.SIZE, 27f);
		textAttributes.put(TextAttribute.WIDTH,
				TextAttribute.WIDTH_SEMI_EXTENDED);
		textAttributes.put(TextAttribute.JUSTIFICATION, 0.5f);
		font = this.getFont().deriveFont(textAttributes);

		drawFormattedString(gameDispName, font, 0, 5, alignment.CENTERED);
		textAttributes.clear();
		height = height - 20;
		font = new Font("Arial", Font.PLAIN, 10);
		int reCount = 0;
		try {
			reCount = Integer.parseInt(reprintCount);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		int sideWaysMargin = 15;
		if (reCount > 0) {
			drawFormattedString("Duplicate", font, sideWaysMargin, 0,
					alignment.CENTERED);
		}

		// setting purchase date and time
		g.setFont(font);
		drawFormattedString(purchaseTimeArr[0], font, sideWaysMargin, 0,
				alignment.LEFTALONGRIGHT);
		drawFormattedString(purchaseTimeArr[1], font, sideWaysMargin, 0,
				alignment.RIGHTMARGIN);

		// printing ticket number
		int constX = 15;
		// drawFormattedString("TicNo : " + data, font, constX, 10,
		// alignment.LEFTMARGIN);

		str = "----------------------------------------------------";
		drawFormattedString(str, font, 0, -5, alignment.CENTERED);

		// draw date and draw time headers
		str = "Draw Date";
		drawFormattedString(str, font, sideWaysMargin, -5,
				alignment.LEFTALONGRIGHT);

		str = "Draw Time";
		drawFormattedString(str, font, sideWaysMargin, -5,
				alignment.RIGHTMARGIN);

		for (int ctr = 0; ctr < drawDateTimeArr.length; ctr++) {
			String[] dateArr = drawDateTimeArr[ctr].split(" ");
			drawFormattedString(dateArr[0], font, sideWaysMargin, 0,
					alignment.LEFTALONGRIGHT);
			drawFormattedString(dateArr[1], font, sideWaysMargin, 0,
					alignment.RIGHTMARGIN);
		}

		str = "----------------------------------------------------";
		drawFormattedString(str, font, 0, -5, alignment.CENTERED);

		if (gameName.equals("Lotto") || gameName.equals("Zimlotto")
				|| gameName.equals("Fastlotto")) {
			for (int ctr = 0; ctr < pickedNumbersArr.length; ctr++)
				drawFormattedString(pickedNumbersArr[ctr], font, 0, 0,
						alignment.CENTERED);

		} else if (gameName.equals("Fortune") || gameName.equals("Zerotonine")
				|| gameName.equals("CARD16") || gameName.equals("Card12")) {
			String winningResult = null;
			/*
			 * System.out.println(pickedNumbers + "****" + betAmountMultiple +
			 * "***" + pickedNumbersArr.length);
			 */
			for (int ctr = 0; ctr < pickedNumbersArr.length; ctr++) {
				pickedNumVal = Integer.parseInt(pickedNumbersArr[ctr].replace(
						"[", "").replace("]", "").replace(" ", ""));
				betAmtVal = Integer.parseInt(betAmountMultArr[ctr].replace("[",
						"").replace("]", "").replace(" ", ""));
				winningResult = symbolArr[pickedNumVal - 1];
				drawFormattedString(winningResult, font, sideWaysMargin, 0,
						alignment.LEFTALONGRIGHT);
				drawFormattedString(betAmtVal + "", font, sideWaysMargin, 0,
						alignment.RIGHTMARGIN);

			}
		} else {
			/*
			 * textAttributes.clear(); textAttributes.put(TextAttribute.WEIGHT,
			 * 1600f); textAttributes.put(TextAttribute.FAMILY, "Arial");
			 * textAttributes.put(TextAttribute.SIZE, 10f); font =
			 * this.getFont().deriveFont(textAttributes);
			 */
			font = new Font("Arial", Font.BOLD, 10);
			drawFormattedString(playType, font, sideWaysMargin, -5,
					alignment.LEFTMARGIN);
			// textAttributes.clear();
			font = new Font("Arial", Font.PLAIN, 10);

			String tempData;

			if (playType.equalsIgnoreCase("banker")) {
				drawFormattedString("Upper Line", font, 0, 0,
						alignment.CENTERED);
				drawFormattedString(pickedNumbers.substring(0,
						pickedNumbers.indexOf(",UL")).replaceAll(",", "-"),
						font, 0, 0, alignment.CENTERED);
				drawFormattedString("Below Line", font, 0, 0,
						alignment.CENTERED);
				// drawFormattedString(pickedNumbers.substring(pickedNumbers.indexOf(",UL,")+4,pickedNumbers.indexOf(",BL")),
				// font, 0, 0, alignment.CENTERED);
				tempData = pickedNumbers.substring(pickedNumbers
						.indexOf(",UL,") + 4, pickedNumbers.indexOf(",BL"));
			} else {
				tempData = pickedNumbers;
			}
			tempData = tempData.replaceAll(",", "-");
			if (tempData.length() > 30) {
				int i = 0;
				int pickNumLen = tempData.length();
				for (int x = 0; x <= pickNumLen / 30; x++) {
					drawFormattedString(tempData.substring(i,
							i + 30 > pickNumLen ? pickNumLen : i + 30), font,
							0, 0, alignment.CENTERED);
					i = i + 30;
				}

			} else {
				drawFormattedString(tempData, font, 0, 0, alignment.CENTERED);
			}

			// drawFormattedString(playType, font, 0, 0, alignment.CENTERED);
		}

		str = "----------------------------------------------------";
		// Total Quantity
		drawFormattedString(str, font, 0, -5, alignment.CENTERED);
		drawFormattedString(gameName.equals("Keno") ? "Total No. of Line(s)"
				: "Total Quantity", font, sideWaysMargin, 0,
				alignment.LEFTALONGRIGHT);
		drawFormattedString(totalQuantity + "", font, sideWaysMargin, 0,
				alignment.RIGHTMARGIN);
		// Unit Price
		drawFormattedString("Unit Price(" + currSymbol + ")", font,
				sideWaysMargin, 0, alignment.LEFTALONGRIGHT);
		drawFormattedString(unitPrice + "", font, sideWaysMargin, 0,
				alignment.RIGHTMARGIN);

		// Number of Draws
		drawFormattedString("No. Of Draws", font, sideWaysMargin, 0,
				alignment.LEFTALONGRIGHT);
		drawFormattedString(noOfDraws, font, sideWaysMargin, 0,
				alignment.RIGHTMARGIN);
		// Total Amount
		drawFormattedString("Total Amount(" + currSymbol + ")", font,
				sideWaysMargin, 0, alignment.LEFTALONGRIGHT);
		drawFormattedString(totalPurchaseAmt, font, sideWaysMargin, 0,
				alignment.RIGHTMARGIN);
		height = height - 10;
		// drawFormattedString("SAMPLE NOT FOR SALE", font, 40, 0,
		// alignment.CENTERED);
		// Bar Image
		g.drawImage(newImage, (appletWidth - newImage
				.getWidth(new ImageObserver() {
					public boolean imageUpdate(Image img, int infoflags, int x,
							int y, int width, int height) {
						return true;
					}
				})) / 2, prevyAxis + height, null);

		/* System.out.println(height + " **" + prevyAxis + " **"); */
		height = height + 55;
		/* System.out.println(height + " **" + prevyAxis + " **"); */
		drawFormattedString("SAMPLE NOT FOR SALE", font, 0, 0,
				alignment.CENTERED);
		drawFormattedString("**********************************", font, 0, 0,
				alignment.CENTERED);
	}

	private void preparePWT() {
		appletWidth = this.getWidth();
		try {
			String isValid = this.getParameter("isValid");
			String status = this.getParameter("status");
			String orgName = this.getParameter("orgName");
			String currSymbol = this.getParameter("currSymbol");
			String gameDispName = this.getParameter("gameDispName");
			String ticketNumber = this.getParameter("ticketNumber");
			String reprintCount = this.getParameter("reprintCount");
			String drawDateTime = this.getParameter("drawDateTime");
			String data = ticketNumber + reprintCount;
			String noOfPanels = this.getParameter("noOfPanels");
			String panelsString = this.getParameter("panelsString");
			String totalPay = this.getParameter("totalPay");
			String isReprint = this.getParameter("isReprint");

			String nonWinStr[] = this.getParameter("nonWinStr").split(",");
			String winStr[] = this.getParameter("winStr").split(",");
			String registerStr[] = this.getParameter("registerStr").split(",");
			int totRegister = 0;
			String claimStr[] = this.getParameter("claimStr").split(",");
			String outVerifyStr[] = this.getParameter("outVerifyStr")
					.split(",");
			String pndPayStr[] = this.getParameter("pndPayStr").split(",");
			String resultAwaitedStr[] = this.getParameter("resultAwaitedStr")
					.split(",");
			String drawAmtStr[] = this.getParameter("drawAmtStr").split(",");

			/*
			 * System.out.println("this.getParameter(drawAmtStr)" +
			 * this.getParameter("drawAmtStr"));
			 */
			double totTktAmt = 0.0;
			String[] drawDateTimeArr = drawDateTime.split(",");
			/*
			 * for (int i = 0; i < drawDateTimeArr.length; i++)
			 * System.out.println("drawDateTimeArr" + drawDateTimeArr[i]);
			 * System.out.println("drawDateTimeArr" +
			 * this.getParameter("resultAwaitedStr"));
			 * 
			 * System.out.println("nonWinStr" + this.getParameter("nonWinStr"));
			 * 
			 * for (int i = 0; i < resultAwaitedStr.length; i++)
			 * System.out.println("resultAwaitedStr" + resultAwaitedStr[i]);
			 */

			Font font = new Font("Arial", Font.PLAIN, 10);
			if (isValid.equals("false")) {
				drawFormattedString("Invalid Ticket", font, 0, 0,
						alignment.CENTERED);
				return;
			}

			Map<TextAttribute, Object> textAttributes = new HashMap<TextAttribute, Object>();

			// setting beginning astericks

			drawFormattedString("**********************************", font, 0,
					0, alignment.CENTERED);

			// setting organization name
			textAttributes.put(TextAttribute.WEIGHT, 300f);
			textAttributes.put(TextAttribute.FAMILY, "Letter Gothic Std");
			textAttributes.put(TextAttribute.SIZE, 15f);
			font = this.getFont().deriveFont(textAttributes);
			drawFormattedString(orgName, font, 0, 0, alignment.CENTERED);
			textAttributes.clear();

			textAttributes.put(TextAttribute.WEIGHT, 800f);
			textAttributes.put(TextAttribute.FAMILY, "Letter Gothic Std");
			textAttributes.put(TextAttribute.SIZE, 27f);
			textAttributes.put(TextAttribute.WIDTH,
					TextAttribute.WIDTH_SEMI_EXTENDED);
			textAttributes.put(TextAttribute.JUSTIFICATION, 0.5f);
			font = this.getFont().deriveFont(textAttributes);

			drawFormattedString(gameDispName, font, 0, 5, alignment.CENTERED);
			textAttributes.clear();
			textAttributes.put(TextAttribute.WEIGHT, 400f);
			textAttributes.put(TextAttribute.FAMILY, "Letter Gothic Std");
			textAttributes.put(TextAttribute.SIZE, 15f);
			font = this.getFont().deriveFont(textAttributes);
			height = height - 20;
			drawFormattedString("PWT Verification", font, 0, 0,
					alignment.CENTERED);
			textAttributes.clear();

			// set font to Arial for further tags
			font = new Font("Arial", Font.PLAIN, 10);
			// printing ticket number
			int constX = 15;
			drawFormattedString("TicNo : " + data, font, constX, -5,
					alignment.LEFTMARGIN);
			String str = "";
			int sideWaysMargin = 15;
			if (status.equals("SUCCESS")) {

				for (int ctr = 0; ctr < drawDateTimeArr.length; ctr++) {

					String[] dateArr = drawDateTimeArr[ctr].split(" ");
					drawFormattedString(dateArr[0], font, sideWaysMargin, 0,
							alignment.LEFTALONGRIGHT);
					drawFormattedString(dateArr[1], font, sideWaysMargin, 0,
							alignment.RIGHTMARGIN);
					if (resultAwaitedStr[ctr].equals("true")) {
						drawFormattedString("Result", font, sideWaysMargin, 0,
								alignment.LEFTALONGRIGHT);
						drawFormattedString("Awaited", font, sideWaysMargin, 0,
								alignment.RIGHTMARGIN);
					} else {
						// System.out.println(nonWinStr[ctr]);
						if (!(nonWinStr[ctr].equals("NA") || nonWinStr[ctr]
								.equals("0"))) {
							drawFormattedString(nonWinStr[ctr] + "Panel (s)",
									font, sideWaysMargin, 0,
									alignment.LEFTALONGRIGHT);
							drawFormattedString("TRY AGAIN", font,
									sideWaysMargin, 0, alignment.RIGHTMARGIN);
						}
						if (!(winStr[ctr].equals("NA") || winStr[ctr]
								.equals("0"))) {
							drawFormattedString(winStr[ctr] + "Panel (s)",
									font, sideWaysMargin, 0,
									alignment.LEFTALONGRIGHT);
							drawFormattedString("WIN" + drawAmtStr[ctr], font,
									sideWaysMargin, 0, alignment.RIGHTMARGIN);
						}
						if (!(registerStr[ctr].equals("NA") || registerStr[ctr]
								.equals("0"))) {
							drawFormattedString(registerStr[ctr] + "Panel (s)",
									font, sideWaysMargin, 0,
									alignment.LEFTALONGRIGHT);
							drawFormattedString("REG.REQ.", font,
									sideWaysMargin, 0, alignment.RIGHTMARGIN);
						}
						if (!(claimStr[ctr].equals("NA") || claimStr[ctr]
								.equals("0"))) {
							drawFormattedString(claimStr[ctr] + "Panel (s)",
									font, sideWaysMargin, 0,
									alignment.LEFTALONGRIGHT);
							drawFormattedString("CLAIMED", font,
									sideWaysMargin, 0, alignment.RIGHTMARGIN);
						}
						if (!(pndPayStr[ctr].equals("NA") || pndPayStr[ctr]
								.equals("0"))) {
							drawFormattedString(pndPayStr[ctr] + "Panel (s)",
									font, sideWaysMargin, 0,
									alignment.LEFTALONGRIGHT);
							drawFormattedString("IN PROCESS", font,
									sideWaysMargin, 0, alignment.RIGHTMARGIN);
						}
						if (!(outVerifyStr[ctr].equals("NA") || outVerifyStr[ctr]
								.equals("0"))) {
							drawFormattedString(
									outVerifyStr[ctr] + "Panel (s)", font,
									sideWaysMargin, 0, alignment.LEFTALONGRIGHT);
							drawFormattedString("OUT OF VERIFY", font,
									sideWaysMargin, 0, alignment.RIGHTMARGIN);
						}
						int regCount = 0;
						if (!registerStr[ctr].equals("NA")) {
							regCount = Integer.parseInt(registerStr[ctr]);
						}
						totRegister = totRegister + regCount;
					}
					str = "----------------------------------------------------";
					drawFormattedString(str, font, 0, -5, alignment.CENTERED);
					double outVerifyCount = 0;
					if (!drawAmtStr[ctr].equals("NA")) {
						outVerifyCount = Double.parseDouble(drawAmtStr[ctr]);
					}
					totTktAmt = totTktAmt + outVerifyCount;
				}
			} else if (status.equals("ERROR")) {
				drawFormattedString("ERROR! Please Try Again..", font, 0, 0,
						alignment.CENTERED);
			}

			drawFormattedString("Total Pay" + "(" + currSymbol + ")", font,
					sideWaysMargin, 0, alignment.LEFTALONGRIGHT);
			drawFormattedString(totTktAmt + "", font, sideWaysMargin, 0,
					alignment.RIGHTMARGIN);
			abc = totTktAmt;
			// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+abc);

			if (totRegister != 0) {
				drawFormattedString(totRegister + "Panel (s)", font,
						sideWaysMargin, 0, alignment.LEFTALONGRIGHT);
				drawFormattedString("REG. REQ.", font, sideWaysMargin, 0,
						alignment.RIGHTMARGIN);
			}
			drawFormattedString("**********************************", font, 0,
					0, alignment.CENTERED);

			if (isReprint.equals("true")) {
				prepareTicket();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.print("dslgfsa");
		}

	}

	private void drawFormattedString(String str, Font f, int sideWaysMargin,
			int relativeHeight, Alignment alignment) {
		g.setFont(f);
		fm = g.getFontMetrics();
		rect = fm.getStringBounds(str, g);
		switch (alignment) {
		case CENTERED:
			g.drawString(str, (int) (appletWidth - rect.getWidth()) / 2,
					prevyAxis + height + relativeHeight);
			break;
		case LEFTALONGRIGHT:
		case LEFTMARGIN:
			g.drawString(str, sideWaysMargin, prevyAxis + height
					+ relativeHeight);
			break;
		case RIGHTMARGIN:
			g.drawString(str,
					(int) (appletWidth - rect.getWidth() - sideWaysMargin),
					prevyAxis + height + relativeHeight);
		}
		if (Alignment.valueOf(alignment.toString()) != Alignment.LEFTALONGRIGHT) {
			prevyAxis = prevyAxis + height;
			// we add relativeHeight which can be negative or positive
			height = (int) fm.getLineMetrics(str, g).getHeight()
					+ relativeHeight;
			// System.out.println("HEIGHT ****** = "
			// + (int) fm.getLineMetrics(str, g).getHeight());
		} else {
			prevyAxis = prevyAxis + height;
			height = 0;
		}
	}

	private String[] getWinningArray(GameNameEnum gameNameEnum) {
		switch (gameNameEnum) {
		case Fortune:
			return fortuneData;
		case Card12:
			return card12Data;
		case CARD16:
			return card16Data;
		case Zerotonine:
			return zeroToNineData;
		}
		return null;
	}

	// Method to print the sketch
	public int print(Graphics g, // Graphics context for printing
			PageFormat pageFormat, // The page format
			int pageIndex) // Index number of current page
			throws PrinterException {
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}
		Graphics2D g2D = (Graphics2D) g;

		Rectangle rect = new Rectangle(this.getBounds());
		// Calculate the scale to fit sketch to page
		double scaleX = pageFormat.getImageableWidth() / rect.width;
		double scaleY = pageFormat.getImageableHeight() / rect.height;
		// Get minimum scale factor
		double scale = Math.min(scaleX, scaleY);
		// Move origin to page printing area corner
		g2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		g2D.scale(scale, scale); // Apply scale factor
		g2D.translate(-rect.x, -rect.y); // Move origin to rect top left

		paint(g2D); // Draw the sketch
		return PAGE_EXISTS;
	}

	BufferedImage img;
	Applet app;

	public static void main(String[] args) {

	}
}
