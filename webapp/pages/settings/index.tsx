import Layout from "@/app/components/layout/Layout";
import React, { useState } from "react";
import { Tab, Tabs } from "react-bootstrap";
import MembersTab from "./members";

const Settings = () => {
  const [key, setKey] = useState("members");
  return (
    <Layout title={""}>
      <Tabs
        id="controlled-tab-example"
        activeKey={key}
        onSelect={(k: any) => setKey(k)}
        className="mb-3"
      >
        <Tab eventKey="members" title="Members">
          <MembersTab />
        </Tab>
        {/* <Tab eventKey="integrations" title="Integrations">
          Integrations
        </Tab> */}
      </Tabs>
    </Layout>
  );
};

export default Settings;