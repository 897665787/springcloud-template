            <t-col :span="4">
              <t-form-item label="{columnComment}" name="{columnName}">
                <t-select class="form-item-content" clearable v-model="formData.{columnName}">
                  <t-option v-for="(item, index) in {columnName}Arr" :key="index" :value="item.code" :label="item.name">
                    {{ item.name }}
                  </t-option>
                </t-select>
              </t-form-item>
            </t-col>
