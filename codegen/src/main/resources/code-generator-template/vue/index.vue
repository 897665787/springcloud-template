<template>
  <div class="list-common-table">
    <t-form
      ref="form"
      :data="formData"
      :label-width="110"
      label-align="right"
      colon
      @submit="onSubmit"
      @reset="onReset"
    >
      <t-row>
        <t-col :xs="8" :sm="8" :md="8" :lg="9" :xl="10">
          <t-row :gutter="[24, 24]">
{search_form}          </t-row>
        </t-col>

        <t-col :xs="4" :sm="4" :md="4" :lg="3" :xl="2" class="operation-container">
          <div class="top_bt_area">
            <t-button theme="primary" type="submit"> 搜索 </t-button>
            <t-button type="reset" variant="base" theme="default"> 重置 </t-button>
          </div>
          <div class="bottom_bt_area">
            <t-button theme="primary" variant="base" @click="doExport"> 导出 </t-button>
          </div>
        </t-col>
      </t-row>
    </t-form>

    <div class="operat_area">
      <t-button variant="outline" theme="primary" class="button_area" v-hasPermi="'save'" @click="openAddMask"> 新增 </t-button>
      <t-button variant="outline" theme="danger" class="button_area" v-hasPermi="'remove'" @click="openDeleteMask"> 删除 </t-button>
    </div>

    <div class="table-container">
      <t-table
        :data="listData"
        :columns="COLUMNS"
        :selected-row-keys="selectedRowKeys"
        row-key="id"
        vertical-align="top"
        :hover="true"
        table-layout="fixed"
        :pagination="pagination"
        :loading="dataLoading"
        @page-change="rehandlePageChange"
        @select-change="rehandleSelectChange"
        cellEmptyContent="-"
        bordered
      >
{table_slot}
        <template #ruleOperation="{ row }">
          <t-button theme="danger" variant="text" v-hasPermi="'remove'" @click="singleDelete(row)">删除</t-button>
          <t-button theme="warning" variant="text" v-hasPermi="'update'" @click="openUpdateMask(row)">修改</t-button>
        </template>
      </t-table>
    </div>

    <operation-mask ref="operationMaskRef" :operation-type="operationType" @resetList="onReset" />
    <t-dialog
        placement="center"
        @confirm="onConfirmDelete"
        @close="onConfirmClose"
        theme="warning"
        v-model:visible="deleteMaskView"
        header="删除后无法恢复，确认删除？"
    ></t-dialog>
  </div>
</template>

<script lang="ts">
export default {
  name: '{ModelName}',
};
</script>

<script setup lang="ts">
import OperationMask from './components/OperationMask.vue';
import { t } from '@/locales';
import { MessagePlugin, PageInfo, PrimaryTableCol, TableRowData, TableProps } from 'tdesign-vue-next';
import debounce from 'lodash/debounce';
import type { FormInstanceFunctions } from 'tdesign-vue-next';
import { requestList, requestDelete, requestExport } from './api';
import { requestDict } from '@/api';

const formData = ref({
{form_ref}});
const COLUMNS: PrimaryTableCol[] = [
  {
    colKey: 'row-select',
    type: 'multiple',
    width: 50
  },
{table_col}  {
    title: '操作',
    align: 'center',
    colKey: 'ruleOperation',
    width: 200
  }
];

{init_dict}

let operationType = ref('add');
const operationMaskRef = ref<any>();
const openAddMask = () => {
  operationType.value = 'add';
  operationMaskRef.value.initComponents();
  operationMaskRef.value.showOperationMask = true;
};

const openUpdateMask = (row: any) => {
  operationType.value = 'update';
  const id = row?.id;
  if (id) {
    operationMaskRef.value.initComponents(id);
    operationMaskRef.value.showOperationMask = true;
  } else {
    MessagePlugin.error(`未找到id`);
  }
};
const deleteMaskView = ref(false);
const openDeleteMask = async () => {
  const arr = selectedRowKeys.value;
  if (!arr?.length) {
    MessagePlugin.error('请选择需要删除的行');
    return;
  }
  deleteMaskView.value = true;
};
const onConfirmDelete = async () => {
  try {
    const params = selectedRowKeys.value;
    await wantDelete(params);
    onConfirmClose();
  } catch (error) {
    console.error(error);
  }
};
const onConfirmClose = () => {
  selectedRowKeys.value = [];
  deleteMaskView.value = false;
};
const singleDelete = async (row: any) => {
  console.log(row);
  const id = row?.id;
  if (id) {
    selectedRowKeys.value = [id];
    openDeleteMask();
  }
};
const selectedRowKeys = ref<TableProps['selectedRowKeys']>([]);
const rehandleSelectChange: TableProps['onSelectChange'] = (value) => {
  selectedRowKeys.value = value;
  console.log(value);
  console.log(selectedRowKeys.value);
};

const wantDelete = async (params: Array<string | Number>) => {
  try {
    const res = await requestDelete(params);
    if (res) {
      MessagePlugin.success('删除成功');
      onSubmit();
    }
  } catch (error) {
    console.error(error);
  }
};

const doExport = async () => {
  try {
    const params : any = {
{request_for_list}    };
    await requestExport(params);
  } catch (error) {
    console.error(error);
  }
};

const pagination = ref({
  pageSize: 10,
  total: 0,
  current: 1,
  pageSizeOptions: [],
});
const dataLoading = ref(false);
const rehandlePageChange = (pageInfo: PageInfo, newDataSource: TableRowData[]) => {
  console.log('分页变化', pageInfo, newDataSource);
  pagination.value.current = pageInfo.current;
  console.log(pagination.value.current);
  debounceGetList();
};

const form = ref<FormInstanceFunctions>();
const onSubmit = () => {
  console.log(formData.value);
  pagination.value.current = 1;
  debounceGetList();
};
const onReset = () => {
  pagination.value.current = 1;
  form.value.reset();
  debounceGetList();
};
const listData = ref([]);
const requestForList = async () => {
  try {
    dataLoading.value = true;
    selectedRowKeys.value = [];
    const params = {
      current: pagination.value.current,
      size: pagination.value.pageSize,
{request_for_list}    };
    const { total, list }: any = await requestList(params);
    if (total) pagination.value.total = total;
    if (list) listData.value = list;
  } catch (error) {
    console.error(error);
  } finally {
    dataLoading.value = false;
  }
};
const debounceGetList = debounce(requestForList, 300);
const initPage = async () => {
  debounceGetList();
{init_dict_func}
};
onMounted(() => {
  initPage();
});
</script>

<style lang="less" scoped>
.list-common-table {
  background-color: #ffffff;
  padding: var(--td-comp-paddingTB-xl);
}
.operat_area {
  margin-top: var(--td-comp-margin-xl);
  .button_area {
    margin-left: var(--td-comp-margin-xl);
  }
}
.table-container {
  margin-top: var(--td-comp-margin-xl);
}
.operation-container {
  display: flex;
  flex-direction: column;
  .top_bt_area {
    margin-left: var(--td-comp-margin-xxxxl);
  }
  .bottom_bt_area {
    margin-left: var(--td-comp-margin-xxxxl);
    margin-top: var(--td-comp-margin-xl);
  }
}
</style>
