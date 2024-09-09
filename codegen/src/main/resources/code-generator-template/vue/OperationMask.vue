<template>
  <div class="components_main">
    <t-dialog
      placement="center"
      v-model:visible="showOperationMask"
      :header="showMaskHeader"
      confirmBtn="确定"
      @confirm="onConfirm"
      width="600px"
    >
      <template #body>
        <t-form
          ref="form"
          :data="formData"
          :rules="FORM_RULES"
          reset-type="initial"
          class="base-form"
          label-align="left"
          :label-width="110"
          @submit="onSubmit"
        >
          <div class="form-basic-container">
            <t-row :gutter="[24, 24]">
{add_form}
            </t-row>
          </div>
        </t-form>
      </template>
    </t-dialog>
  </div>
</template>

<script lang="ts">
export default {
  name: 'OperationMask',
};
</script>

<script setup lang="ts">
import { t } from '@/locales';
import type { FormRule, SubmitContext, FormInstanceFunctions } from 'tdesign-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';

import { requestAdd, requestUpdate, requestDetail } from '../api';
import { requestDict } from '@/api';

const emit = defineEmits(['resetList']);

const FORM_RULES: Record<string, FormRule[]> = {
{input_required}};
const formData = ref({
{form_ref}});
const props = defineProps({
  operationType: {
    type: String,
    default: 'add',
  },
});
const showMaskHeader = computed(() => {
  if (props.operationType === 'add') {
    return `新增{modelNameCn}`;
  } else if (props.operationType === 'update') {
    return `修改{modelNameCn}`;
  }
});
const form = ref<FormInstanceFunctions>();
const onSubmit = async (ctx: SubmitContext) => {
  try {
    console.log(formData.value);
    if (ctx.validateResult === true) {
      if (props.operationType === 'add') {
        add();
      } else if (props.operationType === 'update') {
        update();
      }
    }
  } catch (error) {
    console.error(error);
  }
};
const onConfirm = async () => {
  await form.value.submit();
};
const add = async () => {
  try {
    const params = getParams();
    const res = await requestAdd(params);
    if (res) {
      MessagePlugin.success('新增成功');
      emit('resetList');
      showOperationMask.value = false;
    }
  } catch (error) {
    console.error(error);
  }
};
const update = async () => {
  try {
    const params: any = getParams();
    params.id = updateId;
    const res = await requestUpdate(params);
    if (res) {
      MessagePlugin.success('修改成功');
      emit('resetList');
      showOperationMask.value = false;
    }
  } catch (error) {
    console.error(error);
  }
};
const getParams = () => {
  return {
{get_params}  };
};

{init_dict}

const getDetail = async () => {
  try {
    if (!updateId) return;
    const res: any = await requestDetail(updateId);
    const { {get_detail_const} } = res;
{get_detail}  } catch (error) {
    console.error(error);
  }
};
let updateId: any = null;
const initComponents = async (id?: any) => {
  form.value.reset();
  if (id) updateId = id; else updateId = null;
  await getDetail();
{init_dict_func}
};
const showOperationMask = ref(false);
defineExpose({ showOperationMask, initComponents });
</script>

<style lang="less" scoped></style>
