const {columnName}Arr = ref([]);
const {columnName}Map = ref(new Map<String, String>());
const init{ColumnName} = async () => {
  try {
    const res: any = await requestDict('{modelName}.{columnName}');
    if (res?.length) {
      {columnName}Arr.value = res;
      {columnName}Arr.value.forEach((item: any) => {
        {columnName}Map.value.set(item.code, item.name)
      })
    }
  } catch (error) {
    console.error(error);
  }
}
