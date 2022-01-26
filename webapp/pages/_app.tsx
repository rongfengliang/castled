import type { AppProps } from "next/app";
import { ThemeProvider } from "next-themes";
import Meta from "@/app/components/layout/meta";
import "@/styles/main.scss";
import Router, { useRouter } from "next/router";
import React, { useEffect } from "react";
import NProgress from "nprogress";
import ReactNotification from "react-notifications-component";
import { IntercomProvider, useIntercom } from "react-use-intercom";
import { SWRConfig } from "swr";
import axios from "axios";
import SessionProvider, {
  useSession,
} from "@/app/common/context/sessionContext";
import jsUtils from "@/app/common/utils/jsUtils";
import getConfig from "next/config";

Router.events.on("routeChangeStart", () => {
  NProgress.start();
});
Router.events.on("routeChangeComplete", () => {
  jsUtils.scrollToTop();
  NProgress.done();
});
Router.events.on("routeChangeError", () => NProgress.done());

interface OssProps {
  isOss: boolean;
}

const App = ({ Component, pageProps }: AppProps) => {
  const router = useRouter();

  return (
    <SWRConfig
      value={{
        fetcher: (url) => axios.get(url).then((res: any) => res.data),
        dedupingInterval: 3600000,
      }}
    >
      <ThemeProvider
        attribute="class"
        defaultTheme="system"
        disableTransitionOnChange
      >
        <SessionProvider>
          <EnvLoader isOss={pageProps.isOss} />
          <Meta />
          <ReactNotification />
          <IntercomProvider appId="ak93xau2">
            <IntercomLoader />
            <Component {...pageProps} router={router} />
          </IntercomProvider>
        </SessionProvider>
      </ThemeProvider>
    </SWRConfig>
  );
};

App.getInitialProps = async (ctx: any) => {
  const { publicRuntimeConfig } = getConfig();
  return { pageProps: { isOss: publicRuntimeConfig.isOss === "true" } };
};

const EnvLoader = ({ isOss }: OssProps) => {
  const { setIsOss } = useSession();
  useEffect(() => {
    setIsOss(isOss);
  }, []);

  return null;
};

const IntercomLoader = () => {
  const { boot, hardShutdown } = useIntercom();
  const { user, isOss } = useSession();
  useEffect(() => {
    // Shutdown Intercom in OSS mode.
    if (isOss) {
      hardShutdown();
    } else if (user) {
      boot({
        email: user.email,
        name: user.name,
      });
    }
  }, [user?.email, isOss]);
  return null;
};

export default App;
