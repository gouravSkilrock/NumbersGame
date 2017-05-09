/**
 * 
 */
package com.skilrock.lms.coreEngine.service.dge;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This abstract class assigns a name for each service.
 */
public abstract class ServiceName implements Serializable {
	public static final String CARD12 = "card12Module";

	public static final String CARD16 = "card16Module";

	public static final String DRAWGAME = "dataMgmt";
	
	public static final String PLAYGAME = "playMgmt";

	public static final String DATA_MGMT = "dataMgmt";
	
	public static final String TP_DATA_MGMT = "tpDataMgmt";
	
	public static final String DRAW_MGMT = "drawMgmt";
	
	public static final String DRAW_RESULT_MGMT = "drawResultMgmt";

	
	public static final String FASTLOTTO = "fastlottoModule";

	public static final String FORTUNE_MGMT = "playMgmt";
	
	public static final String FORTUNETWO = "fortunetwoModule";

	public static final String FORTUNETHREE = "fortunethreeModule";
	
	public static final String GAMEMGMT = "gameMgmtModule";

	public static final String INVENTORYMGMT = "inventoryMgmtModule";

	public static final String KENO = "kenoModule";
	
	public static final String KENO_MGMT = "playMgmt";
	
	public static final String KENOTWO_MGMT = "playMgmt";
	
	public static final String KENOFOUR_MGMT = "playMgmt";
	
	public static final String KENOFIVE_MGMT = "playMgmt";
	
	public static final String KENOSIX_MGMT = "playMgmt";

	public static final String KENOSEVEN_MGMT = "playMgmt";

	public static final String KENO_EIGHT_MGMT = "playMgmt";

	public static final String RAINBOW_MGMT = "playMgmt";
	
	public static final String BINGOSEVENTYFIVE_MGMT = "playMgmt";

	public static final String SUPERTWO = "supertwoModule";

	public static final String FOURDIGIT = "fourDigitModule";
	
	static Log logger = LogFactory.getLog(ServiceName.class);

	public static final String LOGINMGMT = "loginMgmtModule";

	public static final String LOTTO = "lottoModule";

	public static final String RAFFLE = "playMgmt";

	public static final String DG_RAFFLE_SECOND_CHANCE = "dgraffleModule";

	public static final String RAFFLE_SECOND_CHANCE_MGMT = "playMgmt";

	public static final String SE_RAFFLE_SECOND_CHANCE = "seraffleModule";

	public static final String REPORTS_MGMT = "reportsMgmt";

	public static final String USERMGMT = "userMgmtModule";

	public static final String ZEROTONINE = "zerotonineModule";

	public static final String ZEROTONINE_MGMT = "playMgmt";

	public static final String ZIMLOTTO = "zimlottoModule";
	
	public static final String ZIMLOTTOTWO = "zimlottotwoModule";
	
	public static final String ZIMLOTTOBONUS_MGMT = "playMgmt";
	
	public static final String ZIMLOTTOBONUSTWO_MGMT = "playMgmt";
	
	public static final String ZIMLOTTOBONUSFREE_MGMT = "playMgmt";
	
	public static final String ZIMLOTTOBONUSTWOFREE_MGMT = "playMgmt";
	
	public static final String ZIMLOTTOTHREE = "zimlottothreeModule";
	
	public static final String BUNUSBALLLOTTO = "bonusballlottoModule";
	
	public static final String BUNUSTWOLOTTO = "bonusballtwoModule";
	
	public static final String TANZANIALOTTO = "tanzanialottoModule";
	
	public static final String PWT_MGMT = "pwtMgmt";
	
	public static final String TWELVEBYTWENTYFOUR_MGMT = "playMgmt";

	public static final String ONETOTWELVE_MGMT = "playMgmt";
	
	public static final String ROULETTE_MGMT = "playMgmt";
	
	public static final String PICKTHREE_MGMT = "playMgmt";
	
	public static final String PICKFOUR_MGMT = "playMgmt";
	
	public static final String TENBYTWENTY_MGMT = "playMgmt";
	public static final String TENBYTHIRTY_MGMT = "playMgmt";
	
	public static final String KENONINE_MGMT = "playMgmt";
}