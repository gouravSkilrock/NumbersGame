import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Linear extends Canvas implements Serializable {

	public Linear() {
		barcodeType = 13;
		byteArray = new int[13];
		codeSup = "";
		demoPixelW = 0;
		codeTextW = 0;
		textMarginCM = 0.29999999999999999D;
		textMarginPixels = 0;
		XdimPixels = 0;
		fXdimPixels = 0.0F;
		checkCharacter = true;
		checkCharacterInText = true;
		processTilde = true;
		postnetHeightTallBar = 0.29999999999999999D;
		postnetHeightShortBar = 0.125D;
		leftMarginCM = 0.29999999999999999D;
		topMarginCM = 0.20000000000000001D;
		leftMarginPixels = 0;
		topMarginPixels = 0;
		endOfCode = 0;
		supplement = "";
		guardBars = true;
		backgroundColor = Color.white;
		codeText = "";
		narrowBarPixels = 0;
		widthBarPixels = 0;
		narrowBarCM = 0.0D;
		widthBarCM = 0.0D;
		resolution = 38;
		barHeightPixels = 0;
		barHeightCM = 1.0D;
		width = 170;
		height = 90;
		pWidth = width;
		pHeight = height;
		autoSize = true;
		showText = true;
		textFont = new Font("Arial", 0, 11 * (int) (resolution * 0.027D));
		textFontColor = Color.black;
		barColor = Color.black;
		extraHeight = 0;
		UPCESystem = '0';
		CodabarStartChar = 'A';
		CodabarStopChar = 'B';
		UPCEANSupplement2 = false;
		UPCEANSupplement5 = false;
		Code128Set = '0';
		X = 0.029999999999999999D;
		N = 2D;
		I = 1.0D;
		H = 0.45000000000000001D;
		L = 0.0D;
		rotateAngle = 0;
		supSeparationCM = 0.5D;
		supHeight = 0.80000000000000004D;
		currentX = 0;
		currentY = 0;
		currentXStart = 0;
		propWhiteBarIncrease = 0.0F;
		whiteBarIncrease = 0.0F;
		valSpliceText = 0;
		bearerBarVert = 0.0D;		
		bearerBarHori = 0.0D;
		barcodeType = Linear.CODE128;
		X = 0.03;
		leftMarginCM = 0.1;
		topMarginCM = 0.1;
		barColor = Color.black;
		textFontColor = Color.black;
		backgroundColor = Color.white;
		textFont = new Font("Arial", 0, 12);
		Code128Set = '0';
		barHeightCM = 0.8;
		showText = true;
		processTilde = true;

		textMarginCM = 0.1;
		whiteBarIncrease = 0;
		bearerBarHori = 0;
		bearerBarVert = 0;
	}

	public void setBarcodeType(int i) {
		barcodeType = i;
		invalidate();
	}

	public int getBarcodeType() {
		return barcodeType;
	}

	public void setBarcode(String s) {
		barcode = s;
		invalidate();
	}

	public String getDataToEncode() {
		return barcode;
	}

	public void setTextMarginCM(double d1) {
		if (d1 > 10D)
			d1 = 10D;
		if (!showText)
			d1 = 0.29999999999999999D;
		textMarginCM = d1;
		invalidate();
	}

	public double getTextMarginCM() {
		return textMarginCM;
	}

	public void setCheckCharacter(boolean flag) {
		checkCharacter = flag;
		invalidate();
	}

	public boolean getCheckCharacter() {
		return checkCharacter;
	}

	public void setCheckCharacterInText(boolean flag) {
		checkCharacterInText = flag;
		invalidate();
	}

	public boolean getCheckCharacterInText() {
		return checkCharacterInText;
	}

	public boolean getProcessTilde() {
		return processTilde;
	}

	public void setProcessTilde(boolean flag) {
		processTilde = flag;
	}

	public void setPostnetHeightTall(double d1) {
		postnetHeightTallBar = d1;
		invalidate();
	}

	public double getPostnetHeightTall() {
		return postnetHeightTallBar;
	}

	public void setPostnetHeightShort(double d1) {
		postnetHeightShortBar = d1;
		invalidate();
	}

	public double getPostnetHeightShort() {
		return postnetHeightShortBar;
	}

	public void setLeftMarginCM(double d1) {
		if (bearerBarVert > 0.0D && d1 < 10D * X)
			d1 = 10D * X;
		leftMarginCM = d1;
		invalidate();
	}

	public double getLeftMarginCM() {
		return leftMarginCM;
	}

	public void setTopMarginCM(double d1) {
		topMarginCM = d1;
		invalidate();
	}

	public double getTopMarginCM() {
		return topMarginCM;
	}

	public void setSupplementToEncode(String s) {
		if (s.length() == 2) {
			UPCEANSupplement2 = true;
			UPCEANSupplement5 = false;
		}
		if (s.length() == 5) {
			UPCEANSupplement5 = true;
			UPCEANSupplement2 = false;
		}
		if (s.length() == 2 || s.length() == 5)
			supplement = s;
		else
			supplement = "";
		invalidate();
	}

	public String getSupplementToEncode() {
		return supplement;
	}

	@Override
	public void setBackground(Color color) {
		backgroundColor = color;
		invalidate();
	}

	@Override
	public Color getBackground() {
		return backgroundColor;
	}

	public void setPixelsPerCM(int i) {
		resolution = i;
		invalidate();
	}

	public int getPixelsPerCM() {
		return resolution;
	}

	public void setBarHeightCM(double d1) {
		barHeightCM = d1;
		invalidate();
	}

	public double getBarHeightCM() {
		return barHeightCM;
	}

	public void setAutoSize(boolean flag) {
		autoSize = flag;
		invalidate();
	}

	public boolean getAutoSize() {
		return autoSize;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(pWidth, pHeight);
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension dimension = new Dimension(10, 10);
		return dimension;
	}

	public void setShowText(boolean flag) {
		showText = flag;
		invalidate();
	}

	public boolean getShowText() {
		return showText;
	}

	@Override
	public void setFont(Font font) {
		textFont = font;
		invalidate();
	}

	@Override
	public Font getFont() {
		return textFont;
	}

	public void setTextFontColor(Color color) {
		textFontColor = color;
		invalidate();
	}

	public Color getTextFontColor() {
		return textFontColor;
	}

	@Override
	public void setForeground(Color color) {
		barColor = color;
		invalidate();
	}

	@Override
	public Color getForeground() {
		return barColor;
	}

	public void setUPCESystem(String s) {
		if (s.equals("0"))
			UPCESystem = '0';
		if (s.equals("1"))
			UPCESystem = '1';
		if (s.equals("2"))
			UPCESystem = '2';
		invalidate();
	}

	public String getUPCESystem() {
		String s = "";
		if (UPCESystem == '0')
			s = "0";
		if (UPCESystem == '1')
			s = "1";
		if (UPCESystem == '2')
			s = "2";
		return s;
	}

	public void setCodabarStartChar(String s) {
		if (s.equals("B"))
			CodabarStartChar = 'B';
		if (s.equals("A"))
			CodabarStartChar = 'A';
		if (s.equals("C"))
			CodabarStartChar = 'C';
		if (s.equals("D"))
			CodabarStartChar = 'D';
		invalidate();
	}

	public String getCodabarStartChar() {
		String s = "";
		if (CodabarStartChar == 'B')
			s = "B";
		if (CodabarStartChar == 'A')
			s = "A";
		if (CodabarStartChar == 'C')
			s = "C";
		if (CodabarStartChar == 'D')
			s = "D";
		return s;
	}

	public void setCodabarStopChar(String s) {
		if (s.equals("B"))
			CodabarStopChar = 'B';
		if (s.equals("A"))
			CodabarStopChar = 'A';
		if (s.equals("C"))
			CodabarStopChar = 'C';
		if (s.equals("D"))
			CodabarStopChar = 'D';
		invalidate();
	}

	public String getCodabarStopChar() {
		String s = "";
		if (CodabarStopChar == 'B')
			s = "B";
		if (CodabarStopChar == 'A')
			s = "A";
		if (CodabarStopChar == 'C')
			s = "C";
		if (CodabarStopChar == 'D')
			s = "D";
		return s;
	}

	public void setUPCEANSupplement2(boolean flag) {
		UPCEANSupplement2 = flag;
		invalidate();
	}

	public boolean getUPCEANSupplement2() {
		return UPCEANSupplement2;
	}

	public void setUPCEANSupplement5(boolean flag) {
		UPCEANSupplement5 = flag;
		invalidate();
	}

	public boolean getUPCEANSupplement5() {
		return UPCEANSupplement5;
	}

	public void setCode128Set(String s) {
		if (s.toUpperCase().equals("B"))
			Code128Set = 'B';
		if (s.toUpperCase().equals("A"))
			Code128Set = 'A';
		if (s.toUpperCase().equals("C"))
			Code128Set = 'C';
		if (s.toUpperCase().equals("AUTO"))
			Code128Set = '0';
		if (s.toUpperCase().equals("0"))
			Code128Set = '0';
		invalidate();
	}

	public String getCode128Set() {
		String s = "";
		if (Code128Set == 'B')
			s = "B";
		if (Code128Set == 'A')
			s = "A";
		if (Code128Set == 'C')
			s = "C";
		if (Code128Set == '0')
			s = "0";
		return s;
	}

	public void setXDimensionCM(double d1) {
		X = d1;
		invalidate();
	}

	public double getXDimensionCM() {
		return X;
	}

	public void setNarrowToWideRatio(double d1) {
		N = d1;
		invalidate();
	}

	public double getNarrowToWideRatio() {
		return N;
	}

	public void setRotationAngle(int i) {
		rotateAngle = i;
		invalidate();
	}

	public int getRotationAngle() {
		return rotateAngle;
	}

	public void setWhiteBarIncrease(float f) {
		propWhiteBarIncrease = f;
		if (propWhiteBarIncrease < 1.0F && propWhiteBarIncrease != 0.0F)
			propWhiteBarIncrease = 1.0F;
		if (propWhiteBarIncrease > 99F)
			propWhiteBarIncrease = 99F;
		if (f >= 1.0F && f <= 99F)
			f /= 100F;
		whiteBarIncrease = f;
		invalidate();
	}

	public float getWhiteBarIncrease() {
		return propWhiteBarIncrease;
	}

	protected void addBar(Graphics g, int i, boolean flag, int j, boolean flag1) {
		if (flag && flag1) {
			g.setColor(barColor);
			g.fillRect(currentX, topMarginPixels + j, i,
					(barHeightPixels + extraHeight) - j);
		}
		currentX = currentX + i;
		fXdimPixels = XdimPixels;
		currentX = currentX + (int) (fXdimPixels * whiteBarIncrease);
	}

	protected String UPCEANCheck(String s) {
		boolean flag = true;
		int i = 0;
		int j = 0;
		int k = 0;
		for (int l = s.length() - 1; l >= 0; l--) {
			if (flag)
				i += (new Integer("" + s.charAt(l))).intValue();
			else
				j += (new Integer("" + s.charAt(l))).intValue();
			flag = !flag;
		}

		j = i * 3 + j;
		k = (int) mod(j, 10D);
		if (k != 0)
			k = 10 - k;
		return "" + k;
	}

	private int findInArray(String as[], String s) {
		for (int i = 0; i < as.length; i++)
			if (as[i].compareTo(s) == 0)
				return i;

		return -1;
	}

	protected boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	protected void paintCode128(Graphics g, boolean flag) {

		String s2 = barcode;
		codeText = barcode;

		int l1 = barcode.length();
		if (processTilde) {
			s2 = "";
			for (int i4 = 1; i4 <= l1; i4++) {
				if (i4 < l1 - 2 && barcode.charAt(i4 - 1) == '~'
						&& barcode.charAt(i4) == 'm') {
					String s5 = "";
					for (int i5 = 0; i5 < l1 - 4; i5++)
						if (barcode.charAt(i5) == '~' && i5 < l1 - 3
								&& Character.isDigit(barcode.charAt(i5 + 1))
								&& Character.isDigit(barcode.charAt(i5 + 2))
								&& Character.isDigit(barcode.charAt(i5 + 3)))
							i5 += 3;
						else
							s5 = s5 + barcode.charAt(i5);

					int i2 = (barcode.charAt(i4 + 1) - 48) * 10
							+ (barcode.charAt(i4 + 2) - 48);
					if (s5.length() - i2 < 1)
						i2 = s5.length();
					int k2 = s5.length() - i2;
					int j3 = i2 + k2;
					String s6 = s5.substring(k2, j3);
					String s4 = UPCEANCheck(s6);
					s2 = s2 + s4;
					i4 += 3;
					continue;
				}
				if (i4 < l1 - 2 && barcode.charAt(i4 - 1) == '~'
						&& barcode.charAt(i4) > '/' && barcode.charAt(i4) < ':') {
					int i1 = (barcode.charAt(i4) - 48) * 100
							+ (barcode.charAt(i4 + 1) - 48) * 10
							+ (barcode.charAt(i4 + 2) - 48);
					if (i1 > -1 && i1 < 255) {
						s2 = s2 + (char) i1;
						i4 += 3;
					} else {
						s2 = s2 + barcode.charAt(i4 - 1);
					}
				} else {
					s2 = s2 + barcode.charAt(i4 - 1);
				}
			}
			barcode = s2;
			s2 = "";
			l1 = barcode.length();
		}
		if (barcodeType == 17 && barcode.charAt(0) != '\312'
				&& (barcode.charAt(0) < '\324' || barcode.charAt(0) > '\331'))
			barcode = '\312' + barcode;
		l1 = barcode.length();
		String as[] = set128B;
		int j2 = 103;
		if (Code128Set != '0') {
			s2 = "";
			codeText = "";
			for (int l2 = 1; l2 <= l1; l2++) {
				int l = barcode.charAt(l2 - 1);
				if (l < 32 && l >= 0) {
					if (Code128Set == 'A')
						s2 = s2 + (char) (l + 96);
					if (Code128Set == 'B')
						if (barcode.charAt(l2) < ' ') {
							s2 = s2 + '\311' + (char) (l + 96)
									+ (char) (barcode.charAt(l2) + 96) + '\310';
							l2++;
						} else {
							s2 = s2 + '\311' + (char) (l + 96) + '\310';
						}
					if (Code128Set == 'C')
						if (barcode.charAt(l2) < ' ') {
							s2 = s2 + '\311' + '\311' + (l + 64)
									+ (barcode.charAt(l2) + 64) + "99";
							l2++;
						} else {
							s2 = s2 + '\311' + '\311' + (l + 64) + "99";
						}
					if (l == 13 || l == 9)
						codeText = codeText + "  ";
				} else {
					codeText = codeText + (char) l;
					s2 = s2 + (char) l;
				}
			}
		}
		if (Code128Set == '0') {
			as = set128B;
			codeText = "";
			s2 = "";
			char c2 = '\314';
			byte byte0 = 66;
			char c = barcode.charAt(0);
			if (c < ' ')
				c2 = '\313';
			if (c > '\037' && c < '\177')
				c2 = '\314';
			if (l1 > 3 && c > '/' && c < ':' && barcode.charAt(1) > '/'
					&& barcode.charAt(1) < ':' && barcode.charAt(2) > '/'
					&& barcode.charAt(2) < ':' && barcode.charAt(3) > '/'
					&& barcode.charAt(3) < ':')
				c2 = '\315';
			if (c == '\305')
				c2 = '\314';
			if (c == '\312' || c > '\323')
				c2 = '\315';
			if (c2 == '\313') {
				byte0 = 65;
				j2 = 103;
				paintChar(g, "bwbwbw", "211412", flag);
			}
			if (c2 == '\314') {
				byte0 = 66;
				j2 = 104;
				paintChar(g, "bwbwbw", "211214", flag);
			}
			if (c2 == '\315') {
				byte0 = 67;
				j2 = 105;
				paintChar(g, "bwbwbw", "211232", flag);
			}
			for (int l3 = 1; l3 <= l1; l3++) {
				int j1 = barcode.charAt(l3 - 1);
				if (l3 < l1 - 1 && (j1 == 202 || j1 > 211)) {
					s2 = s2 + '\312';
					continue;
				}
				if (j1 == 197) {
					if (byte0 == 67) {
						s2 = s2 + '\310';
						byte0 = 66;
					}
					s2 = s2 + '\305';
					continue;
				}
				if (l3 <= l1 - 3 && j1 > 47 && j1 < 58
						&& barcode.charAt(l3) > '/' && barcode.charAt(l3) < ':'
						&& barcode.charAt(l3 + 1) > '/'
						&& barcode.charAt(l3 + 1) < ':'
						&& barcode.charAt(l3 + 2) > '/'
						&& barcode.charAt(l3 + 2) < ':' || l3 <= l1 - 1
						&& j1 > 47 && j1 < 58 && barcode.charAt(l3) > '/'
						&& barcode.charAt(l3) < ':' && byte0 == 67) {
					if (byte0 != 67) {
						int j4 = l3;
						int l4 = 3;
						for (; j4 <= l1 && isDigit(barcode.charAt(j4 - 1)); j4++)
							l4 = 4 - l4;

						if (l4 == 1) {
							s2 = s2 + barcode.charAt(l3 - 1);
							l3++;
						}
						l4 = 0;
					}
					if (byte0 != 67) {
						s2 = s2 + '\307';
						byte0 = 67;
					}
					j1 = (barcode.charAt(l3 - 1) - 48) * 10
							+ (barcode.charAt(l3) - 48);
					if (j1 < 95 && j1 >= 0)
						s2 = s2 + (char) (j1 + 32);
					else if (j1 > 94)
						s2 = s2 + (char) (j1 + 100);
					l3++;
					continue;
				}
				if (l3 <= l1 && (j1 < 32 || byte0 == 65 && j1 < 96)) {
					if (byte0 != 65) {
						s2 = s2 + '\311';
						byte0 = 65;
					}
					if (j1 < 32) {
						s2 = s2 + (char) (j1 + 96);
						continue;
					}
					if (j1 > 31)
						s2 = s2 + (char) j1;
					continue;
				}
				if (l3 > l1 || j1 <= 31 || j1 >= 127)
					continue;
				if (byte0 != 66) {
					s2 = s2 + '\310';
					byte0 = 66;
				}
				s2 = s2 + (char) j1;
			}

			for (int k4 = 1; k4 <= l1; k4++) {
				char c1 = barcode.charAt(k4 - 1);
				boolean flag5 = false;
				if (k4 < l1 - 1 && (c1 == '\312' || c1 > '\323')) {
					int k1 = (barcode.charAt(k4) - 48) * 10
							+ (barcode.charAt(k4 + 1) - 48);
					if (barcode.charAt(k4 - 1) == '\324') {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1) + ") ";
						k4 += 2;
						flag5 = true;
						continue;
					}
					if (k4 < l1 - 2 && barcode.charAt(k4 - 1) == '\325') {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1)
								+ barcode.charAt(k4 + 2) + ") ";
						k4 += 3;
						flag5 = true;
						continue;
					}
					if (k4 < l1 - 3 && barcode.charAt(k4 - 1) == '\326') {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1)
								+ barcode.charAt(k4 + 2)
								+ barcode.charAt(k4 + 3) + ") ";
						k4 += 4;
						flag5 = true;
						continue;
					}
					if (k4 < l1 - 4 && barcode.charAt(k4 - 1) == '\327') {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1)
								+ barcode.charAt(k4 + 2)
								+ barcode.charAt(k4 + 3)
								+ barcode.charAt(k4 + 4) + ") ";
						k4 += 5;
						flag5 = true;
						continue;
					}
					if (k4 < l1 - 5 && barcode.charAt(k4 - 1) == '\330') {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1)
								+ barcode.charAt(k4 + 2)
								+ barcode.charAt(k4 + 3)
								+ barcode.charAt(k4 + 4)
								+ barcode.charAt(k4 + 5) + ") ";
						k4 += 6;
						flag5 = true;
						continue;
					}
					if (k4 < l1 - 6 && barcode.charAt(k4 - 1) == '\331') {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1)
								+ barcode.charAt(k4 + 2)
								+ barcode.charAt(k4 + 3)
								+ barcode.charAt(k4 + 4)
								+ (int) barcode.charAt(k4 + 5)
								+ barcode.charAt(k4 + 6) + ") ";
						k4 += 7;
						flag5 = true;
						continue;
					}
					if (k1 <= 30 && k1 >= 0 || k1 <= 99 && k1 >= 90) {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1) + ") ";
						k4 += 2;
						flag5 = true;
						continue;
					}
					if (k4 < l1 - 2
							&& (k1 <= 49 && k1 >= 40 || k1 <= 25 && k1 >= 23)) {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1)
								+ barcode.charAt(k4 + 2) + ") ";
						k4 += 3;
						flag5 = true;
						continue;
					}
					if (k4 < l1 - 3
							&& (k1 <= 81 && k1 >= 80 || k1 <= 34 && k1 >= 31)) {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1)
								+ barcode.charAt(k4 + 2)
								+ barcode.charAt(k4 + 3) + ") ";
						k4 += 4;
						flag5 = true;
						continue;
					}
					if (k4 < l1 - 3 && !flag5) {
						codeText = codeText + " (" + barcode.charAt(k4)
								+ barcode.charAt(k4 + 1)
								+ barcode.charAt(k4 + 2)
								+ barcode.charAt(k4 + 3) + ") ";
						k4 += 4;
					}
					continue;
				}
				if (barcode.charAt(k4 - 1) < ' ') {
					codeText = codeText + " ";
					continue;
				}
				if (barcode.charAt(k4 - 1) == '\305') {
					codeText = codeText + " ";
					continue;
				}
				if (barcode.charAt(k4 - 1) > '\037'
						&& barcode.charAt(k4 - 1) < '\200')
					codeText = codeText + barcode.charAt(k4 - 1);
			}
		}
		if (Code128Set == 'B') {
			as = set128B;
			j2 = 104;
		}
		if (Code128Set == 'C') {
			as = set128C;
			j2 = 105;
			if (s2.length() % 2 == 1) {
				s2 = "0" + s2;
				codeText = s2;
			}
		}
		if (Code128Set == 'B')
			paintChar(g, "bwbwbw", "211214", flag);
		if (Code128Set == 'C')
			paintChar(g, "bwbwbw", "211232", flag);
		if (Code128Set == 'A')
			paintChar(g, "bwbwbw", "211412", flag);
		int i3 = 1;
		for (int k3 = 0; k3 < s2.length(); k3++) {
			String s = "" + s2.charAt(k3);
			if (Code128Set == 'C') {
				String s1 = "" + s;
				if (++k3 < s2.length())
					s1 = s1 + s2.charAt(k3);
				int i = findInArray(set128C, s1);
				if (i >= 0) {
					paintChar(g, "bwbwbw", set128[i], flag);
					j2 += i * i3;
				}
			} else {
				int j = findInArray(as, s);
				if (j >= 0) {
					paintChar(g, "bwbwbw", set128[j], flag);
					j2 += j * i3;
				}
			}
			i3++;
		}

		if (checkCharacter) {
			int k = (int) mod(j2, 103D);
			paintChar(g, "bwbwbw", set128[k], flag);
		}
		paintChar(g, "bwbwbwb", "2331112", flag);
	}

	protected static double mod(double d1, double d2) {
		double d3 = d1 / d2;
		double d4 = Math.round(d3);
		if (d4 > d3)
			d4--;
		return d1 - d2 * d4;
	}

	protected void paintChar(Graphics g, String s, String s1, boolean flag) {
		paintChar2(g, s, s1, 0, flag);
	}

	protected void paintChar2(Graphics g, String s, String s1, int i,
			boolean flag) {
		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);
			char c1 = s1.charAt(j);
			if (c1 == 'n')
				addBar(g, narrowBarPixels, c == 'b', i, flag);
			if (c1 == 'w')
				addBar(g, widthBarPixels, c == 'b', i, flag);
			if (c1 == '1')
				addBar(g, narrowBarPixels, c == 'b', i, flag);
			if (c1 == '2')
				addBar(g, narrowBarPixels * 2, c == 'b', i, flag);
			if (c1 == '3')
				addBar(g, narrowBarPixels * 3, c == 'b', i, flag);
			if (c1 == '4')
				addBar(g, narrowBarPixels * 4, c == 'b', i, flag);
		}

	}

	protected void calculateSizes() {
		narrowBarCM = X;
		widthBarCM = X * N;
		if (barHeightCM == 0.0D) {
			barHeightCM = L * H;
			if (barHeightCM < 0.625D)
				barHeightCM = 0.625D;
		}
		if (barHeightCM != 0.0D)
			barHeightPixels = (int) (barHeightCM * resolution);
		if (narrowBarCM != 0.0D)
			narrowBarPixels = (int) (narrowBarCM * resolution);
		if (widthBarCM != 0.0D)
			widthBarPixels = (int) (narrowBarPixels * N);
		if (narrowBarPixels <= 0)
			narrowBarPixels = 1;
		if (widthBarPixels <= 1)
			widthBarPixels = 2;
	}

	@Override
	public void paint(Graphics g) {
		Graphics g1 = g;
		Image image = null;
		textMarginPixels = (int) (textMarginCM * resolution);
		g1.setFont(new Font("Arial", 0, 11));
		int i = g1.getFontMetrics().getHeight();
		g1.setFont(new Font(textFont.getName(), textFont.getStyle(),
				(int) ((textFont.getSize() * resolution) * 0.027D)));
		if (rotateAngle != 0) {
			String javaVersion = System.getProperty("java.version");
			if (javaVersion.indexOf("1.0") == 0
					|| javaVersion.indexOf("1.1") == 0) {
				image = createImage(getSize().width, getSize().height);
				g1 = image.getGraphics();
			} else {
				int a = getSize().width;
				int b = getSize().height;
				int k = a;
				if (b > a)
					k = b;
				image = new BufferedImage(k, k, 13);
				g1 = ((BufferedImage) image).createGraphics();

			}
		}
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
		paintBasis(g1);
		if (rotateAngle != 0) {
			if (!showText)
				textMarginPixels = 0;
			int l = currentY + topMarginPixels + textMarginPixels + i
					+ (int) (bearerBarHori * narrowBarPixels);
			if (barcodeType == 11 || barcodeType == 10 || barcodeType == 12
					|| barcodeType == 6)
				l += (int) (bearerBarVert * narrowBarPixels);
			if (!showText)
				l += (int) (bearerBarVert * narrowBarPixels);

		}
	}

	protected void paintBasis(Graphics g) {

		codeText = "";
		g.setFont(new Font("Arial", 0, 11));
		demoPixelW = g.getFontMetrics().stringWidth("SkilRock 1 Technology");
		calculateSizes();
		topMarginPixels = (int) (topMarginCM * resolution);
		leftMarginPixels = (int) (leftMarginCM * resolution);
		XdimPixels = (int) (X * resolution);
		g.setFont(new Font(textFont.getName(), textFont.getStyle(),
				(int) ((textFont.getSize() * resolution) * 0.027D)));
		int j = g.getFontMetrics().getHeight();
		g.setColor(backgroundColor);
		int k = getSize().width;
		int l = getSize().height;
		int i1 = k;
		if (l > i1)
			i1 = l;
		g.fillRect(0, 0, i1, i1);
		endOfCode = 0;
		currentX = leftMarginPixels;

		if (barcodeType == 13)
			paintCode128(g, false);

		if (endOfCode == 0)
			endOfCode = currentX;
		L = (currentX - leftMarginPixels) / resolution;
		if (codeText.length() == 0)
			codeText = barcode;
		g.setFont(new Font("Arial", 0, 11));
		demoPixelW = g.getFontMetrics().stringWidth("Skilrock  2 Technology");
		g.setFont(new Font(textFont.getName(), textFont.getStyle(),
				(int) ((textFont.getSize() * resolution) * 0.027D)));
		codeTextW = g.getFontMetrics().stringWidth(codeText);
		if (barcodeType == 18)
			codeTextW += leftMarginPixels;
		int k1 = currentX - leftMarginPixels;
		k1 += 2 * leftMarginPixels;
		k1 += (int) (2D * (bearerBarVert * narrowBarPixels));
		int l1 = 0;
		if (!showText)
			codeTextW = 0;
		int i2 = 0;
		if (valSpliceText > 0)
			i2 = codeText.length() / valSpliceText - 1;
		codeTextW += g.getFontMetrics().stringWidth(" ") * i2;
		if (barcodeType == 18) {
			int j2 = g.getFontMetrics().stringWidth(" ");
			if (codeText.length() == 31)
				codeTextW += 6 * j2;
			if (codeText.length() == 29)
				codeTextW += 5 * j2;
			if (codeText.length() == 25)
				codeTextW += 4 * j2;
			if (codeText.length() == 20)
				codeTextW += 3 * j2;
		}
		if (codeTextW >= demoPixelW && codeTextW >= k1)
			pWidth = codeTextW;
		if (demoPixelW >= codeTextW && demoPixelW >= k1)
			pWidth = demoPixelW;
		if (k1 >= codeTextW && k1 >= demoPixelW)
			pWidth = k1;
		currentX = (pWidth / 2 - k1 / 2) + leftMarginPixels
				+ (int) (bearerBarVert * narrowBarPixels);
		if (barcodeType == 18)
			currentX = leftMarginPixels;
		l1 = pWidth / 2 - codeTextW / 2;
		textMarginPixels = (int) (textMarginCM * resolution);
		g.setFont(new Font("Arial", 0, 11));
		g.setFont(new Font(textFont.getName(), textFont.getStyle(),
				(int) ((textFont.getSize() * resolution) * 0.027D)));
		if (showText && textFont != null) {
			g.setFont(new Font(textFont.getName(), textFont.getStyle(),
					(int) ((textFont.getSize() * resolution) * 0.027D)));
			pHeight = barHeightPixels + j + topMarginPixels + textMarginPixels
					+ (int) (bearerBarHori * narrowBarPixels);
		} else {
			pHeight = barHeightPixels + topMarginPixels + textMarginPixels
					+ (int) (bearerBarHori * narrowBarPixels);
		}
		int l2 = 7;
		if (barcodeType == 13)
			paintCode128(g, true);

		if (endOfCode == 0)
			endOfCode = currentX;
		currentY = barHeightPixels + topMarginPixels;
		if (autoSize)
			if (rotateAngle == 0 || rotateAngle == 180)
				setSize(pWidth, pHeight);
			else
				setSize(pHeight, pWidth);
		if (barcodeType == 15 || barcodeType == 16)
			currentY = (int) (postnetHeightTallBar * resolution) + 1
					+ topMarginPixels;
		g.setFont(new Font(textFont.getName(), textFont.getStyle(),
				(int) ((textFont.getSize() * resolution) * 0.027D)));
		j = g.getFontMetrics().getHeight();
		codeTextW = g.getFontMetrics().stringWidth(codeText);
		l2 += j;
		if (showText && textFont != null) {
			g.setColor(textFontColor);
			j = g.getFontMetrics().getHeight();
			if ((UPCEANSupplement2 || UPCEANSupplement5)
					&& (barcodeType == 11 || barcodeType == 6
							|| barcodeType == 12 || barcodeType == 10)) {
				int k3 = (endSuplement - startSuplement - g.getFontMetrics()
						.stringWidth(codeSup)) / 2;
				if (k3 < 0)
					k3 = 0;
				g.drawString(codeSup, startSuplement + k3,
						(topMarginPixels + suplementTopMargin) - 2);
			}
			int l3 = 0;

			int k5 = (endOfCode - leftMarginPixels - g.getFontMetrics()
					.stringWidth(codeText)) / 2;
			if (codeText == "Incorrect Data To Encode") {
				g.drawString(codeText, l1, barHeightPixels + textMarginPixels
						+ l3 + topMarginPixels);
			} else {
				if (k5 < 0)
					k5 = 0;
				g.drawString(codeText, l1, barHeightPixels + (int) (0.5D * j)
						+ textMarginPixels + topMarginPixels
						+ (int) (bearerBarHori * narrowBarPixels));
				currentY = barHeightPixels + j + 1 + topMarginPixels
						+ (int) (bearerBarHori * narrowBarPixels);
			}
		}
	}

	public static final int CODE128 = 13;

	public int barcodeType;
	static int TABLE_2_OF_13_SIZE;
	static int TABLE_5_OF_13_SIZE;
	static boolean Table2of13InitializedFlag = false;
	static boolean Table5of13InitializedFlag = false;
	static long NumberOf5of13TableEntries = 0L;
	static long NumberOf2of13TableEntries = 0L;
	static short Remainder = 0;
	static int Table2of13[];
	static int Table5of13[];
	
	static int Table5of13ArrayPtr[];
	static int Table2of13ArrayPtr[];
	static double CodewordArray[][] = new double[10][2];
	int byteArray[];
	public String barcode;
	private final String codeSup;
	private int demoPixelW;
	private int codeTextW;
	public double textMarginCM;
	private int textMarginPixels;
	private int XdimPixels;
	float fXdimPixels;
	public boolean checkCharacter;
	public boolean checkCharacterInText;
	public boolean processTilde;
	public double postnetHeightTallBar;
	public double postnetHeightShortBar;
	public double leftMarginCM;
	protected static final int d = 3;
	public double topMarginCM;
	protected int leftMarginPixels;
	protected int topMarginPixels;
	private int endOfCode;
	private int startSuplement;
	private int endSuplement;
	private int suplementTopMargin;
	public String supplement;
	public boolean guardBars;
	public Color backgroundColor;
	public String codeText;
	protected int narrowBarPixels;
	protected int widthBarPixels;
	protected double narrowBarCM;
	protected double widthBarCM;
	public int resolution;
	protected int barHeightPixels;
	public double barHeightCM;
	public int width;
	public int height;
	public int pWidth;
	public int pHeight;
	public boolean autoSize;
	public boolean showText;
	public Font textFont;
	public Color textFontColor;
	public Color barColor;
	private final int extraHeight;
	public char UPCESystem;
	public char CodabarStartChar;
	public char CodabarStopChar;
	public boolean UPCEANSupplement2;
	public boolean UPCEANSupplement5;
	public char Code128Set;
	public double X;
	public double N;
	public double I;
	public double H;
	public double L;
	public int rotateAngle;
	public double supSeparationCM;
	public double supHeight;
	protected int currentX;
	protected int currentY;
	protected int currentXStart;
	protected String set39[][] = { { "0", "nnnwwnwnn" }, { "1", "wnnwnnnnw" },
			{ "2", "nnwwnnnnw" }, { "3", "wnwwnnnnn" }, { "4", "nnnwwnnnw" },
			{ "5", "wnnwwnnnn" }, { "6", "nnwwwnnnn" }, { "7", "nnnwnnwnw" },
			{ "8", "wnnwnnwnn" }, { "9", "nnwwnnwnn" }, { "A", "wnnnnwnnw" },
			{ "B", "nnwnnwnnw" }, { "C", "wnwnnwnnn" }, { "D", "nnnnwwnnw" },
			{ "E", "wnnnwwnnn" }, { "F", "nnwnwwnnn" }, { "G", "nnnnnwwnw" },
			{ "H", "wnnnnwwnn" }, { "I", "nnwnnwwnn" }, { "J", "nnnnwwwnn" },
			{ "K", "wnnnnnnww" }, { "L", "nnwnnnnww" }, { "M", "wnwnnnnwn" },
			{ "N", "nnnnwnnww" }, { "O", "wnnnwnnwn" }, { "P", "nnwnwnnwn" },
			{ "Q", "nnnnnnwww" }, { "R", "wnnnnnwwn" }, { "S", "nnwnnnwwn" },
			{ "T", "nnnnwnwwn" }, { "U", "wwnnnnnnw" }, { "V", "nwwnnnnnw" },
			{ "W", "wwwnnnnnn" }, { "X", "nwnnwnnnw" }, { "Y", "wwnnwnnnn" },
			{ "Z", "nwwnwnnnn" }, { "-", "nwnnnnwnw" }, { ".", "wwnnnnwnn" },
			{ " ", "nwwnnnwnn" }, { "$", "nwnwnwnnn" }, { "/", "nwnwnnnwn" },
			{ "+", "nwnnnwnwn" }, { "%", "nnnwnwnwn" }, { "*", "nwnnwnwnn" } };
	protected String set25[][] = { { "0", "nnwwn" }, { "1", "wnnnw" },
			{ "2", "nwnnw" }, { "3", "wwnnn" }, { "4", "nnwnw" },
			{ "5", "wnwnn" }, { "6", "nwwnn" }, { "7", "nnnww" },
			{ "8", "wnnwn" }, { "9", "nwnwn" } };
	protected String setMSI[][] = { { "0", "nwnwnwnw" }, { "1", "nwnwnwwn" },
			{ "2", "nwnwwnnw" }, { "3", "nwnwwnwn" }, { "4", "nwwnnwnw" },
			{ "5", "nwwnnwwn" }, { "6", "nwwnwnnw" }, { "7", "nwwnwnwn" },
			{ "8", "wnnwnwnw" }, { "9", "wnnwnwwn" } };
	protected String set11[][] = { { "0", "nnnnw" }, { "1", "wnnnw" },
			{ "2", "nwnnw" }, { "3", "wwnnn" }, { "4", "nnwnw" },
			{ "5", "wnwnn" }, { "6", "nwwnn" }, { "7", "nnnww" },
			{ "8", "wnnwn" }, { "9", "wnnnn" }, { "-", "nnwnn" } };
	protected String setCODABAR[][] = { { "0", "nnnnnww" }, { "1", "nnnnwwn" },
			{ "2", "nnnwnnw" }, { "3", "wwnnnnn" }, { "4", "nnwnnwn" },
			{ "5", "wnnnnwn" }, { "6", "nwnnnnw" }, { "7", "nwnnwnn" },
			{ "8", "nwwnnnn" }, { "9", "wnnwnnn" }, { "-", "nnnwwnn" },
			{ "$", "nnwwnnn" }, { ":", "wnnnwnw" }, { "/", "wnwnnnw" },
			{ ".", "wnwnwnn" }, { "+", "nnwnwnw" }, { "A", "nnwwnwn" },
			{ "B", "nwnwnnw" }, { "C", "nnnwnww" }, { "D", "nnnwwwn" } };
	protected String set93[][] = { { "0", "131112" }, { "1", "111213" },
			{ "2", "111312" }, { "3", "111411" }, { "4", "121113" },
			{ "5", "121212" }, { "6", "121311" }, { "7", "111114" },
			{ "8", "131211" }, { "9", "141111" }, { "A", "211113" },
			{ "B", "211212" }, { "C", "211311" }, { "D", "221112" },
			{ "E", "221211" }, { "F", "231111" }, { "G", "112113" },
			{ "H", "112212" }, { "I", "112311" }, { "J", "122112" },
			{ "K", "132111" }, { "L", "111123" }, { "M", "111222" },
			{ "N", "111321" }, { "O", "121122" }, { "P", "131121" },
			{ "Q", "212112" }, { "R", "212211" }, { "S", "211122" },
			{ "T", "211221" }, { "U", "221121" }, { "V", "222111" },
			{ "W", "112122" }, { "X", "112221" }, { "Y", "122121" },
			{ "Z", "123111" }, { "-", "121131" }, { ".", "311112" },
			{ " ", "311211" }, { "$", "321111" }, { "/", "112131" },
			{ "+", "113121" }, { "%", "211131" }, { "_1", "121221" },
			{ "_2", "312111" }, { "_3", "311121" }, { "_4", "122211" } };
	protected String setUPCALeft[][] = { { "0", "3211" }, { "1", "2221" },
			{ "2", "2122" }, { "3", "1411" }, { "4", "1132" }, { "5", "1231" },
			{ "6", "1114" }, { "7", "1312" }, { "8", "1213" }, { "9", "3112" } };
	protected String setUPCARight[][] = { { "0", "3211" }, { "1", "2221" },
			{ "2", "2122" }, { "3", "1411" }, { "4", "1132" }, { "5", "1231" },
			{ "6", "1114" }, { "7", "1312" }, { "8", "1213" }, { "9", "3112" } };
	protected String setUPCEOdd[][] = { { "0", "3211" }, { "1", "2221" },
			{ "2", "2122" }, { "3", "1411" }, { "4", "1132" }, { "5", "1231" },
			{ "6", "1114" }, { "7", "1312" }, { "8", "1213" }, { "9", "3112" } };
	protected String setUPCEEven[][] = { { "0", "1123" }, { "1", "1222" },
			{ "2", "2212" }, { "3", "1141" }, { "4", "2311" }, { "5", "1321" },
			{ "6", "4111" }, { "7", "2131" }, { "8", "3121" }, { "9", "2113" } };
	protected String set39Ext[] = { "%U", "$A", "$B", "$C", "$D", "$E", "$F",
			"$G", "$H", "$I", "$J", "$K", "$L", "$M", "$N", "$O", "$P", "$Q",
			"$R", "$S", "$T", "$U", "$V", "$W", "$X", "$Y", "$Z", "%A", "%B",
			"%C", "%D", "%E", " ", "/A", "/B", "/C", "/D", "/E", "/F", "/G",
			"/H", "/I", "/J", "/K", "/L", "-", ".", "/O", "0", "1", "2", "3",
			"4", "5", "6", "7", "8", "9", "/Z", "%F", "%G", "%H", "%I", "%J",
			"%V", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
			"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
			"Z", "%K", "%L", "%M", "%N", "%O", "%W", "+A", "+B", "+C", "+D",
			"+E", "+F", "+G", "+H", "+I", "+J", "+K", "+L", "+M", "+N", "+O",
			"+P", "+Q", "+R", "+S", "+T", "+U", "+V", "+W", "+X", "+Y", "+Z",
			"%P", "%Q", "%R", "%S", "%T" };
	protected String set93Ext[] = { "_2U", "_1A", "_1B", "_1C", "_1D", "_1E",
			"_1F", "_1G", "_1H", "_1I", "_1J", "_1K", "_1L", "_1M", "_1N",
			"_1O", "_1P", "_1Q", "_1R", "_1S", "_1T", "_1U", "_1V", "_1W",
			"_1X", "_1Y", "_1Z", "_2A", "_2B", "_2C", "_2D", "_2E", " ", "_3A",
			"_3B", "_3C", "_3D", "_3E", "_3F", "_3G", "_3H", "_3I", "_3J",
			"_3K", "_3L", "-", ".", "_3O", "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "_3Z", "_2F", "_2G", "_2H", "_2I", "_2J", "_2V",
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
			"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"_2K", "_2L", "_2M", "_2N", "_2O", "_2W", "_4A", "_4B", "_4C",
			"_4D", "_4E", "_4F", "_4G", "_4H", "_4I", "_4J", "_4K", "_4L",
			"_4M", "_4N", "_4O", "_4P", "_4Q", "_4R", "_4S", "_4T", "_4U",
			"_4V", "_4W", "_4X", "_4Y", "_4Z", "_2P", "_2Q", "_2R", "_2S",
			"_2T" };
	protected String UPCESystem0[] = { "EEEOOO", "EEOEOO", "EEOOEO", "EEOOOE",
			"EOEEOO", "EOOEEO", "EOOOEE", "EOEOEO", "EOEOOE", "EOOEOE" };
	protected String UPCESystem1[] = { "OOOEEE", "OOEOEE", "OOEEOE", "OOEEEO",
			"OEOOEE", "OEEOOE", "OEEEOO", "OEOEOE", "OEOEEO", "OEEOEO" };
	protected String setEANLeftA[][] = { { "0", "3211" }, { "1", "2221" },
			{ "2", "2122" }, { "3", "1411" }, { "4", "1132" }, { "5", "1231" },
			{ "6", "1114" }, { "7", "1312" }, { "8", "1213" }, { "9", "3112" } };
	protected String setEANLeftB[][] = { { "0", "1123" }, { "1", "1222" },
			{ "2", "2212" }, { "3", "1141" }, { "4", "2311" }, { "5", "1321" },
			{ "6", "4111" }, { "7", "2131" }, { "8", "3121" }, { "9", "2113" } };
	protected String setEANRight[][] = { { "0", "3211" }, { "1", "2221" },
			{ "2", "2122" }, { "3", "1411" }, { "4", "1132" }, { "5", "1231" },
			{ "6", "1114" }, { "7", "1312" }, { "8", "1213" }, { "9", "3112" } };
	protected String setEANCode[] = { "AAAAA", "ABABB", "ABBAB", "ABBBA",
			"BAABB", "BBAAB", "BBBAA", "BABAB", "BABBA", "BBABA" };
	protected String fiveSuplement[] = { "EEOOO", "EOEOO", "EOOEO", "EOOOE",
			"OEEOO", "OOEEO", "OOOEE", "OEOEO", "OEOOE", "OOEOE" };
	protected String set128[] = { "212222", "222122", "222221", "121223",
			"121322", "131222", "122213", "122312", "132212", "221213",
			"221312", "231212", "112232", "122132", "122231", "113222",
			"123122", "123221", "223211", "221132", "221231", "213212",
			"223112", "312131", "311222", "321122", "321221", "312212",
			"322112", "322211", "212123", "212321", "232121", "111323",
			"131123", "131321", "112313", "132113", "132311", "211313",
			"231113", "231311", "112133", "112331", "132131", "113123",
			"113321", "133121", "313121", "211331", "231131", "213113",
			"213311", "213131", "311123", "311321", "331121", "312113",
			"312311", "332111", "314111", "221411", "431111", "111224",
			"111422", "121124", "121421", "141122", "141221", "112214",
			"112412", "122114", "122411", "142112", "142211", "241211",
			"221114", "413111", "241112", "134111", "111242", "121142",
			"121241", "114212", "124112", "124211", "411212", "421112",
			"421211", "212141", "214121", "412121", "111143", "111341",
			"131141", "114113", "114311", "411113", "411311", "113141",
			"114131", "311141", "411131" };
	protected String set128B[] = { " ", "!", "\"", "#", "$", "%", "&", "'",
			"(", ")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@", "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[",
			"\\", "]", "^", "_", "`", "a", "b", "c", "d", "e", "f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z", "{", "|", "}", "~", "\303", "\304",
			"\305", "\306", "\307", "\310", "\311", "\312" };
	protected String set128C[] = { "00", "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
			"40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
			"51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61",
			"62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72",
			"73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83",
			"84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94",
			"95", "96", "97", "98", "99", "\310\310", "\311\311", "\312\312" };
	protected String setPOSTNET[][] = { { "0", "11000" }, { "1", "00011" },
			{ "2", "00101" }, { "3", "00110" }, { "4", "01001" },
			{ "5", "01010" }, { "6", "01100" }, { "7", "10001" },
			{ "8", "10010" }, { "9", "10100" } };
	protected String setPLANET[][] = { { "0", "00111" }, { "1", "11100" },
			{ "2", "11010" }, { "3", "11001" }, { "4", "10110" },
			{ "5", "10101" }, { "6", "10011" }, { "7", "01110" },
			{ "8", "01101" }, { "9", "01011" } };
	private float propWhiteBarIncrease;
	public float whiteBarIncrease;
	public int valSpliceText;
	public double bearerBarVert;
	
	public double bearerBarHori;
	

	static {
		TABLE_2_OF_13_SIZE = 78;
		TABLE_5_OF_13_SIZE = 1287;
		Table2of13 = new int[TABLE_2_OF_13_SIZE];
		Table5of13 = new int[TABLE_5_OF_13_SIZE];
		Table5of13ArrayPtr = new int[TABLE_5_OF_13_SIZE];
		Table2of13ArrayPtr = new int[TABLE_2_OF_13_SIZE];
	}

}