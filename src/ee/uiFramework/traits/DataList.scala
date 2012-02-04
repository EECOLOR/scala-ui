package ee.uiFramework.traits

trait DataList[T] {
    private var _dataList:List[T] = _
    
	def dataList = _dataList
	def dataList_=(dataList:List[T]) = {
	    _dataList = dataList
	    
	    processDataList(dataList)
    }
    
    def processDataList(dataList:List[T])
}