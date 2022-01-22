import { ConnectorCategory } from "@/app/common/utils/types";
import documentationService from "@/app/services/documentationService";
import { useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";

export interface IntegratedDocProps {
  category: ConnectorCategory;
  connectorType: string;
}

const IntegratedDoc = ({ category, connectorType }: IntegratedDocProps) => {
  const [markdown, setMarkdown] = useState<string>("");
  useEffect(() => {
    documentationService.load(category, connectorType).then((markdown) => {
      const metaPos = markdown.lastIndexOf("\n---\n");
      setMarkdown(metaPos === -1 ? markdown : markdown.substring(metaPos + 5));
    });
  }, [category, connectorType]);
  return <ReactMarkdown className="integrated-doc">{markdown}</ReactMarkdown>;
};

export default IntegratedDoc;
