						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label{required}">{simpleColumnComment}：</label>
							</div>
							<div class="col-xs-9">
								<select name="{columnName}" class="form-control">
									<xs:descriptionOptions clazz="{module}.{ModelName}" property="{columnName}"/>
								</select>
							</div>
						</div>
