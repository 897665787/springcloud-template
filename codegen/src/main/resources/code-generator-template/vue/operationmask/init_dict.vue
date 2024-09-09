const {columnName}Arr = ref([]);
const init{ColumnName} = async () => {
  try {
    const res: any = await requestDict('{modelName}.{columnName}');
    if (res?.length) {
      {columnName}Arr.value = res;
    }
  } catch (error) {
    console.error(error);
  }
  if (!updateId) {
    formData.value.{columnName} = {columnName}Arr.value[0]['code'];
  }
}
