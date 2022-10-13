package com.dmi.smartux.common.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {
    private int mStartRow = 1;
    private int mStartCol = 1;
    private int mWidth = 5000;

    private IndexedColors mHeaderColor =  IndexedColors.LIGHT_CORNFLOWER_BLUE;
    private IndexedColors mDataColor =  IndexedColors.WHITE;

    private XSSFWorkbook mWorkbook;

    private String mSheetName = "sheet1";

    private List<Object> mHeader;
    private List<List<Object>> mData;
    
    private InputStream mReadFile;

    public ExcelManager(List<Object> header, List<List<Object>> data) {
        mHeader = header;
        mData = data;
    }

    public ExcelManager(InputStream excelFile) {
    	mReadFile = excelFile;
    }

    public void setStartRow(int startRow) {
        mStartRow = startRow;
    }

    public void setStartCol(int startCol) {
        mStartCol = startCol;
    }

    public void setSheetName(String sheetName) {
        mSheetName = sheetName;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeaderColor(IndexedColors headerColor) {
        mHeaderColor = headerColor;
    }

    public void setDataColor(IndexedColors dataColor) {
        mDataColor = dataColor;
    }

    public byte[] makeExcel() {
        mWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = mWorkbook.createSheet(mSheetName);
        XSSFRow headerRow = sheet.createRow(mStartRow);

        int headerCount = mHeader.size();

        for (int i=mStartCol; i<headerCount+mStartCol; i++) {
            XSSFCell headerCell = headerRow.createCell(i, XSSFCell.CELL_TYPE_STRING);
            setCell(headerCell, String.valueOf(mHeader.get(i - mStartCol)), mHeaderColor.getIndex());
            sheet.setColumnWidth(i, mWidth);
        }

        int dataCount = mData.size();

        for (int i=mStartRow+1; i<dataCount+mStartRow+1; i++) {
            XSSFRow dataRow = sheet.createRow(i);

            List<Object> data = mData.get(i - (mStartRow + 1));

            int count = data.size();

            for (int j=mStartCol; j<count+mStartCol; j++) {
                XSSFCell dataCell = dataRow.createCell(j, XSSFCell.CELL_TYPE_STRING);

                Object cell = data.get(j - mStartCol);
                String cellStr;

                if (cell instanceof Date) {
                    cellStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell);
                } else {
                    cellStr = String.valueOf(cell);
                }

                setCell(dataCell, cellStr, mDataColor.getIndex());
                sheet.setColumnWidth(i, mWidth);
            }
        }

        byte[] bytes = new byte[0];

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            mWorkbook.write(out);
            bytes = out.toByteArray();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }
    
    public List<List<Object>> readExcel(){
    	List<List<Object>> excelData = new ArrayList<List<Object>>();
    	List<Object> cellData = null;
    	
        try {
			mWorkbook = new XSSFWorkbook(mReadFile);
			XSSFSheet sheet    =  null;
			XSSFRow row     =  null;
			XSSFCell cell    =  null;
			
			int sheetNum =  mWorkbook.getNumberOfSheets();
			for(int i=0;i<sheetNum;i++){//시트가 여러개 있을 경우
			    sheet = mWorkbook.getSheetAt(i);
			    
			    int lastRowNum = sheet.getLastRowNum();
			    for(int r=sheet.getFirstRowNum();r<=lastRowNum;r++){//row를 읽는다.
			    	row = sheet.getRow(r);
			        if(row== null) continue;
			        
		    		int lastCellNum = row.getLastCellNum();
			        cellData = new ArrayList<Object>();
			        for(int c=row.getFirstCellNum();c<=lastCellNum;c++){//cell을 읽는다.
				        cell   =  row.getCell(c);
				        if(cell== null) continue;
				        
				        switch(cell.getCellType()){
					        case XSSFCell.CELL_TYPE_NUMERIC:
					        	cellData.add(cell.getNumericCellValue());
					        	break;
					        case XSSFCell.CELL_TYPE_STRING:
					        	cellData.add(cell.getStringCellValue());
					        	break;
					        case XSSFCell.CELL_TYPE_FORMULA :
					        	cellData.add(cell.getCellFormula());
					        	break;
					        default:
					        	cellData.add("");
				       }
			        }
				    excelData.add(cellData);
			    }
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return excelData;
    }

    private void setCell(XSSFCell headerCell, String data, short index) {
        headerCell.setCellValue(data);

        XSSFCellStyle cellStyle = mWorkbook.createCellStyle();

        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(index);

        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        headerCell.setCellStyle(cellStyle);
    }
}