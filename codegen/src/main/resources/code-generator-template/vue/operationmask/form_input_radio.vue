              <t-col :span="12">
                <t-form-item label="{columnComment}" name="{columnName}">
                  <t-radio-group v-model="formData.{columnName}">
                    <t-radio v-for="(item, index) in statusArr" :key="index" :value="item.value">{{
                      item.label
                    }}</t-radio>
                  </t-radio-group>
                </t-form-item>
              </t-col>
