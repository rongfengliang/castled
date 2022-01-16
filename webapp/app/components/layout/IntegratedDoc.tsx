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
    documentationService
      .load(category, connectorType)
      .then((markdown) => setMarkdown(markdown));
  }, [category, connectorType]);
  return <ReactMarkdown>{markdown}</ReactMarkdown>;
};

export default IntegratedDoc;
