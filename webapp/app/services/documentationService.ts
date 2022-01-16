import { ConnectorCategory } from "./../common/utils/types";

export default {
  load: (
    connectorCategory: ConnectorCategory,
    connectorType: string
  ): Promise<string> => {
    const folder =
      connectorCategory === "Warehouse" ? "Sources" : "Destinations";
    const docPath = `https://raw.githubusercontent.com/castledio/castled/main/docs/docs/getting-started/${folder}/configure-${connectorType.toLowerCase()}.md`;
    return fetch(docPath).then((res) => res.text());
  },
};
