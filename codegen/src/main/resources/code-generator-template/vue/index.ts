import { requestGet, requestPost, requestDownloadGet } from '@/utils/request';

export function requestList(datas: Record<string, any>) {
  return requestGet('/{modelName}/page', datas);
}

export function requestDetail(id: any) {
  return requestGet('/{modelName}/query', { id });
}

export function requestAdd(datas: Record<string, any>) {
  return requestPost('/{modelName}/save', datas);
}

export function requestUpdate(datas: Record<string, any>) {
  return requestPost('/{modelName}/update', datas);
}

export function requestDelete(datas: Array<any>) {
  return requestPost('/{modelName}/remove', { idList: datas });
}

export function requestExport(datas: Record<string, any>) {
  return requestDownloadGet('/{modelName}/export', datas);
}
