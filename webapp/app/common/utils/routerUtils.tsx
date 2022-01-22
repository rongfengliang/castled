export default {
  getArray: (id: string[] | string | undefined): any => {
    return Array.isArray(id) ? id : id ? [id] : [];
  },
  getString: (id: string[] | string | undefined): any => {
    return Array.isArray(id) ? id[0] : id || "";
  },
  getBoolean: (id: string[] | string | undefined): any => {
    const strValue = Array.isArray(id) ? id[0] : id || "";
    return strValue === "1" || strValue === "true";
  },
  getInt: (id: string[] | string | undefined): any => {
    return Array.isArray(id) ? parseInt(id[0]) : id ? parseInt(id) : null;
  },
};
