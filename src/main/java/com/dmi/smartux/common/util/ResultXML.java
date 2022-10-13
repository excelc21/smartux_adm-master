package com.dmi.smartux.common.util;

import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.vo.ResultVO;

/**
 * 통신 시 공통적으로 사용 되는 flag 및 message 를 대입하는 함수
 * 
 * @author jch82@naver.com
 */
public class ResultXML {

	/**
	 * response 의 결과 값에 성공 값을 설정한다.
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @return 결과 값이 담긴 vo
	 */
	public static <T> T getSuccessResult ( T result ) throws Exception {
		( (ResultVO) result ).setFlag ( SmartUXProperties.getProperty ( "flag.success" ) );
		( (ResultVO) result ).setMessage ( SmartUXProperties.getProperty ( "message.success" ) );
		return result;
	}

	/**
	 * response 의 결과 값에 변경 없음을 설정한다.
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @return 결과 값이 담긴 vo
	 */
	public static <T> T getNoChangeInfoResult ( T result ) throws Exception {
		( (ResultVO) result ).setFlag ( SmartUXProperties.getProperty ( "flag.nochangeinfo" ) );
		( (ResultVO) result ).setMessage ( SmartUXProperties.getProperty ( "message.nochangeinfo" ) );
		return result;
	}

	/**
	 * response 의 결과 값에 넘버 포멧이 틀렸음을 설정한다.
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @return 결과 값이 담긴 vo
	 */
	public static <T> T getNumberFormatExceptionResult ( T result, String parameterName ) throws Exception {
		( (ResultVO) result ).setFlag ( SmartUXProperties.getProperty ( "flag.numberformat" ) );
		( (ResultVO) result ).setMessage ( SmartUXProperties.getProperty ( "message.numberformat", parameterName ) );
		return result;
	}

	/**
	 * response 의 결과 값에 찾는 결과 값 없음을 설정한다.
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @return 결과 값이 담긴 vo
	 */
	public static <T> T getNotFoundResult ( T result ) throws Exception {
		( (ResultVO) result ).setFlag ( SmartUXProperties.getProperty ( "flag.notfound" ) );
		( (ResultVO) result ).setMessage ( SmartUXProperties.getProperty ( "message.notfound" ) );
		return result;
	}

	/**
	 * response 의 결과 값에 파라메터 값이 없음을 설정한다.
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @return 결과 값이 담긴 vo
	 */
	public static <T> T getParamNotFoundResult ( T result, String parameterName ) throws Exception {
		( (ResultVO) result ).setFlag ( SmartUXProperties.getProperty ( "flag.paramnotfound" ) );
		( (ResultVO) result ).setMessage ( SmartUXProperties.getProperty ( "message.paramnotfound", parameterName ) );
		return result;
	}

	/**
	 * response 의 결과 값에 DB Error 를 설정한다.
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @return 결과 값이 담긴 vo
	 */
	public static <T> T getDBErrorResult ( T result ) throws Exception {
		( (ResultVO) result ).setFlag ( SmartUXProperties.getProperty ( "flag.db" ) );
		( (ResultVO) result ).setMessage ( SmartUXProperties.getProperty ( "message.db" ) );
		return result;
	}

	/**
	 * 표현형식 : {paramName} 파라미터는 값이 {message} 이어야 함
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @param paramName 잘못 된 파라메터의 명칭
	 * @param message 파라메터 값이 무엇이 잘못 되었는지 작성
	 * @return 결과 값이 담긴 vo
	 * @throws Exception
	 */
	public static <T> T getMismatchValueResult ( T result, String paramName, String message ) throws Exception {
		( (ResultVO) result ).setFlag ( SmartUXProperties.getProperty ( "flag.mismatchvalue" ) );
		( (ResultVO) result ).setMessage ( SmartUXProperties.getProperty ( "message.mismatchvalue", paramName, message ) );
		return result;
	}

	/**
	 * exception 발생 시 response 의 결과 값에 발생 된 exception 정보를 설정한다.
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @return 결과 값이 담긴 vo
	 */
	public static <T> T getExceptionResult ( T result, Exception exception ) {
		ExceptionHandler handler = new ExceptionHandler ( exception );
		( (ResultVO) result ).setFlag ( handler.getFlag ( ) );
		( (ResultVO) result ).setMessage ( handler.getMessage ( ) );
		return result;
	}

	/**
	 * exception 발생 시 response 의 결과 값에 발생 된 exception 정보를 설정한다.
	 * 
	 * @param resultVO 결과가 담길 vo
	 * @return 결과 값이 담긴 vo
	 */
	public static <T> T getEtcErrorResult ( T result ) {
		( (ResultVO) result ).setFlag ( "9999" );
		( (ResultVO) result ).setMessage ( "기타 오류" );
		return result;
	}

}
