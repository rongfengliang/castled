import { ConnectorCategory } from "@/app/common/utils/types";
import documentationService from "@/app/services/documentationService";
import React from "react";
import { useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";

export interface IntegratedDocProps {
  category: ConnectorCategory;
  connectorType: string;
}

const IntegratedDoc = ({ category, connectorType }: IntegratedDocProps) => {
  const [markdown, setMarkdown] = useState<string>("");

  let docImgUrl =
    process.env.DOC_APP_IMAGE_BASE_URL ||
    "https://raw.githubusercontent.com/castledio/castled/main/docs/static/"; // NP: This can be deleted and url can be loaded only from env.

  useEffect(() => {
    documentationService.load(category, connectorType).then((markdown) => {
      const metaPos = markdown.lastIndexOf("\n---\n");
      setMarkdown(metaPos === -1 ? markdown : markdown.substring(metaPos + 5));
    });
  }, [category, connectorType]);
  return (
    <ReactMarkdown
      className="integrated-doc"
      transformImageUri={(uri) =>
        uri.startsWith("http") ? uri : `${docImgUrl}${uri}`
      }
    >
      {markdown}
    </ReactMarkdown>
  );
};

export default IntegratedDoc;
