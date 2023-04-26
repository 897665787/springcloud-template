                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">{simpleColumnComment}：</label>
                    	</div>
                        <div class="col-xs-8 col-md-4 col-lg-7 m-b-md">
                            <div class="row">
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="dynamic[{columnName}Start]" class="form-control datepicker"
                                           value="${search.dynamic.{columnName}Start}" readonly>
                                </div>
                                <label class="pull-left control-label" style="width: 15px">至</label>
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="dynamic[{columnName}End]" class="form-control datepicker"
                                           value="${search.dynamic.{columnName}End}" readonly>
                                </div>
                            </div>
                        </div>
