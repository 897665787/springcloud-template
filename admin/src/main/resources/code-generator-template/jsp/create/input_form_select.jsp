                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label{required}">{simpleColumnComment}：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3 m-b-md">
                            <select name="{columnName}" class="form-control">
                                <xs:descriptionOptions clazz="{module}.{ModelName}" property="{columnName}"/>
                            </select>
                        </div>
