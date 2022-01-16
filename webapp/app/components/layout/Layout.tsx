import Header, { HeaderProps } from "@/app/components/layout/Header";
import React from "react";
import LeftSidebar from "@/app/components/layout/LeftSidebar";
import HeadCommon from "@/app/components/layout/HeadCommon";
import { WizardSteps } from "@/app/common/dtos/internal/WizardSteps";

interface LayoutProps extends HeaderProps {
  children: React.ReactNode;
  steps?: WizardSteps;
  stepGroups?: WizardSteps;
  rightHelp?: React.ReactNode;
}

const Layout = ({
  title,
  centerTitle,
  pageTitle,
  navLinks,
  rightBtn,
  children,
  steps,
  stepGroups,
  rightHelp,
}: LayoutProps) => {
  return (
    <div className="layout-holder">
      <HeadCommon title={typeof title === "string" ? title : pageTitle || ""} />
      <LeftSidebar />
      <Header
        title={title}
        centerTitle={centerTitle}
        navLinks={navLinks}
        rightBtn={rightBtn}
        steps={steps}
        stepGroups={stepGroups}
      />
      {renderChildren(children, rightHelp)}
    </div>
  );
};

const renderChildren = (
  children: React.ReactNode,
  rightHelp: React.ReactNode
) => {
  if (rightHelp) {
    return (
      <>
        <main className="row">
          <div className="container-fluid container-main row">
            <div className="col-8">{children}</div>
            <div className="col-4">{rightHelp}</div>
          </div>
        </main>
      </>
    );
  } else {
    return (
      <>
        <main>
          <div className="container-fluid container-main">{children}</div>
        </main>
      </>
    );
  }
};

export default Layout;
