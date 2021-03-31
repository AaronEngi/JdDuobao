/**
  * Copyright 2021 bejson.com 
  */
package tyrael.duobao.jdapi;
import java.util.List;

/**
 * Auto-generated: 2021-03-31 9:11:15
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private int pageSize;
    private int pageNo;
    private int totalCount;
    private List<PageList> pageList;
    private String cursorMark;
    public void setPageSize(int pageSize) {
         this.pageSize = pageSize;
     }
     public int getPageSize() {
         return pageSize;
     }

    public void setPageNo(int pageNo) {
         this.pageNo = pageNo;
     }
     public int getPageNo() {
         return pageNo;
     }

    public void setTotalCount(int totalCount) {
         this.totalCount = totalCount;
     }
     public int getTotalCount() {
         return totalCount;
     }

    public void setPageList(List<PageList> pageList) {
         this.pageList = pageList;
     }
     public List<PageList> getPageList() {
         return pageList;
     }

    public void setCursorMark(String cursorMark) {
         this.cursorMark = cursorMark;
     }
     public String getCursorMark() {
         return cursorMark;
     }

}