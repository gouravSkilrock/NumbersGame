package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DecoderUtilityForPwt {
	static Log logger = LogFactory.getLog(DecoderUtilityForPwt.class);

	/*
	 * public VirnCodeBean fetchDecyptedVirnDetails(String virn, String
	 * encSchemeType){
	 * 
	 * ArrayList<VirnCodeBean> virnBeanList = null;
	 * if("1W_ENC_OF_ALL".equalsIgnoreCase(encSchemeType.trim())) {// 1W
	 * Encryption of virn, ticket and code virnBeanList =
	 * create1WEncVirnBeanListWithFixedCode(bean); }else
	 * if("2W_ENC_OF_TKT".equalsIgnoreCase(encSchemeType.trim())){ //2W
	 * Encryption of ticket(key is virn+code) and 1W encryption of virn & code
	 * virnBeanList = create2WEncVirnBeanListWithFixedCode(bean); }else
	 * if("2W_ENC_OF_TKT_VIRN".equalsIgnoreCase(encSchemeType.trim())){ //2W
	 * Encryption of virn & ticket(that are already 1W Encrypted ) using
	 * code(1W) virnBeanList = create2WEncVirnBeanList(bean); } }
	 * 
	 * 
	 * public static void main(String[] args) { }
	 * 
	 * /** This method is used when we using the '1W Encryption' to create the
	 * virn list to insert into database. @param br BufferReader @param
	 * encVirnStrBuilder @param gameNbrDigits @param maxRankDigits @param
	 * game_id @param rankDetailMap @return @throws NumberFormatException
	 * @throws IOException
	 * 
	 * private VirnCodeBean create1WEncVirnBeanListWithFixedCode(VirnCodeBean
	 * virnBean ) throws NumberFormatException, IOException{
	 * 
	 * 
	 * 
	 * 
	 * String virn_code = null; String encoded_virn_code=null;
	 * 
	 * final String DEFAULT_KEY = "1234"; String enckey = null, encTicket =
	 * null; String virnDetArr[] = new String[3];
	 * 
	 * virnDetArr[0] = virnDetArr[0].trim(); //virn_code virnDetArr[1] =
	 * virnDetArr[1].trim(); // ticket_nbr // DEFAULT_KEY //verification_code
	 * 
	 * 
	 * 
	 * virn_code = virnBean.getVirn_code(); // un Encrypted // 1W encryption of
	 * virn , ticket and key is done encoded_virn_code =
	 * MD5Encoder.encode(virn_code); enckey = MD5Encoder.encode(DEFAULT_KEY);
	 * encTicket = MD5Encoder.encode(virnDetArr[1]);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * virnBean.setStatus("UNCLM_PWT");
	 * virnBean.setVirn_code(encoded_virn_code); virnBean.setId1(encTicket);
	 * virnBean.setId2(enckey);
	 * 
	 * 
	 * 
	 * 
	 * return virnBean; }
	 * 
	 * 
	 * 
	 *//**
		 * This method is used when we using the '2W Encryption for ticket with
		 * fixed code' to create the virn list to insert into database.
		 * 
		 * @param br
		 *            BufferReader
		 * @param encVirnStrBuilder
		 * @param gameNbrDigits
		 * @param maxRankDigits
		 * @param game_id
		 * @param rankDetailMap
		 * @return
		 * @throws NumberFormatException
		 * @throws IOException
		 * @throws EncryptionException
		 * @throws NoSuchPaddingException
		 * @throws NoSuchAlgorithmException
		 */
	/*
	 * private ArrayList<VirnCodeBean>
	 * create2WEncVirnBeanListWithFixedCode(VirnCodeBean bean ) throws
	 * NumberFormatException, IOException, EncryptionException,
	 * NoSuchAlgorithmException, NoSuchPaddingException{
	 * 
	 * VirnCodeBean virnBean = null; String strLine = null; int rank_id = 0;
	 * String virn_code = null; String encoded_virn_code=null, encodedTkt =
	 * null, encKey = null; RankDetailBean rankDetailBean = null; ArrayList<VirnCodeBean>
	 * virnBeanList = new ArrayList<VirnCodeBean>(50000);
	 * encVirnStrBuilder.ensureCapacity(50000*15); String virnDetArr[];
	 * VirnEncoderNDecoder encoder = new VirnEncoderNDecoder(); final String
	 * DEFAULT_KEY = "1234"; //final String DEFAULT_ENCRYPTION_KEY =
	 * MD5Encoder.encode(DEFAULT_KEY); //1234 while((strLine
	 * =br.readLine())!=null) { if("".equals(strLine.trim())) continue;
	 * 
	 * virnDetArr = strLine.split("\t");
	 * 
	 * virnDetArr[0] = virnDetArr[0].trim(); //virn_code virnDetArr[1] =
	 * virnDetArr[1].trim(); // ticket_nbr // DEFAULT_KEY; //verification_code //
	 * get the rank id and virn_code rank_id =
	 * Integer.parseInt(virnDetArr[0].substring(gameNbrDigits,
	 * gameNbrDigits+maxRankDigits)); virn_code =
	 * strLine.substring(gameNbrDigits+maxRankDigits, virnDetArr[0].length()); //
	 * 2W encryption of ticket using (virn+code) encodedTkt =
	 * encoder.encrypt(virnDetArr[1], (virn_code+DEFAULT_KEY)) ;
	 * encoded_virn_code = MD5Encoder.encode(virn_code); encKey =
	 * MD5Encoder.encode(DEFAULT_KEY);
	 * 
	 * 
	 * encVirnStrBuilder.append("'").append(encoded_virn_code).append("',"); //
	 * get the prize amount and status detail of game rankDetailBean =
	 * rankDetailMap.get(rank_id); // virn bean is created virnBean = new
	 * VirnCodeBean(); virnBean.setVirn_code(encoded_virn_code);
	 * virnBean.setGame_id(game_id);
	 * virnBean.setPwt_amt(rankDetailBean.getPrize_amount());
	 * virnBean.setPrize_level(rankDetailBean.getStatus());
	 * virnBean.setStatus("UNCLM_PWT"); virnBean.setId1(encodedTkt);
	 * virnBean.setId2(encKey); virnBeanList.add(virnBean); }
	 * 
	 * virnBeanList.trimToSize(); encVirnStrBuilder.trimToSize();
	 * 
	 * return virnBeanList; }
	 * 
	 * 
	 *//**
		 * This method is used when we using the '2W Encryption of virn(1W
		 * Encoded) & ticket(1W Encoded) with variable code(1W)' to create the
		 * virn list to insert into database.
		 * 
		 * @param br
		 *            BufferReader
		 * @param encVirnStrBuilder
		 * @param gameNbrDigits
		 * @param maxRankDigits
		 * @param game_id
		 * @param rankDetailMap
		 * @return
		 * @throws NumberFormatException
		 * @throws IOException
		 * @throws EncryptionException
		 * @throws NoSuchPaddingException
		 * @throws NoSuchAlgorithmException
		 */
	/*
	 * private ArrayList<VirnCodeBean> create2WEncVirnBeanList(VirnCodeBean
	 * bean ) throws NumberFormatException, IOException, EncryptionException,
	 * NoSuchAlgorithmException, NoSuchPaddingException{
	 * 
	 * VirnCodeBean virnBean = null; String strLine = null; int rank_id = 0;
	 * String virn_code = null; String encoded_virn_code=null, encodedTkt =
	 * null, enckey = null; RankDetailBean rankDetailBean = null; ArrayList<VirnCodeBean>
	 * virnBeanList = new ArrayList<VirnCodeBean>(50000);
	 * encVirnStrBuilder.ensureCapacity(50000*15); String virnDetArr[];
	 * VirnEncoderNDecoder encoder = new VirnEncoderNDecoder(); while((strLine
	 * =br.readLine())!=null) { if("".equals(strLine.trim())) continue;
	 * 
	 * virnDetArr = strLine.split("\t");
	 * 
	 * virnDetArr[0] = virnDetArr[0].trim(); //virn_code virnDetArr[1] =
	 * virnDetArr[1].trim(); // ticket_nbr virnDetArr[2] = virnDetArr[2].trim();
	 * //verification_code // get the rank id and virn_code rank_id =
	 * Integer.parseInt(virnDetArr[0].substring(gameNbrDigits,
	 * gameNbrDigits+maxRankDigits)); virn_code =
	 * strLine.substring(gameNbrDigits+maxRankDigits, virnDetArr[0].length()); //
	 * encrypt the virn, ticket and code using 1W encryption encoded_virn_code =
	 * MD5Encoder.encode(virn_code); encodedTkt =
	 * MD5Encoder.encode(virnDetArr[1]); enckey =
	 * MD5Encoder.encode(virnDetArr[2]); // 2W encryption of ticket using
	 * (virn(1W)+code(1W)) encoded_virn_code =
	 * encoder.encrypt(encoded_virn_code, enckey) ; encodedTkt =
	 * encoder.encrypt(encodedTkt, enckey) ; // get the prize amount and status
	 * detail of game rankDetailBean = rankDetailMap.get(rank_id); // virn bean
	 * is created virnBean = new VirnCodeBean();
	 * virnBean.setVirn_code(encoded_virn_code); virnBean.setGame_id(game_id);
	 * virnBean.setPwt_amt(rankDetailBean.getPrize_amount());
	 * virnBean.setPrize_level(rankDetailBean.getStatus());
	 * virnBean.setStatus("UNCLM_PWT"); virnBean.setId1(encodedTkt);
	 * virnBean.setId2(""); //virnBean.setId2(enckey);
	 * virnBeanList.add(virnBean);
	 * 
	 * encVirnStrBuilder.append("'").append(encoded_virn_code).append("',"); }
	 * 
	 * virnBeanList.trimToSize(); encVirnStrBuilder.trimToSize();
	 * 
	 * return virnBeanList; }
	 */

}
