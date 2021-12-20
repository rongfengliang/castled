import type { AppProps } from "next/app";
import { ThemeProvider } from "next-themes";
import Meta from "@/app/components/layout/meta";
import "@/styles/main.scss";
import Router, { useRouter } from "next/router";
import React, { useEffect } from "react";
import NProgress from "nprogress";
import ReactNotification from "react-notifications-component";
import { IntercomProvider, useIntercom } from 'react-use-intercom';
import { SWRConfig } from "swr";
import eventService from "@/app/services/eventService";
import axios from "axios";
import SessionProvider, {
  useSession,
} from "@/app/common/context/sessionContext";
import jsUtils from "@/app/common/utils/jsUtils";
import { domain } from "process";

Router.events.on("routeChangeStart", () => {
  NProgress.start();
});
Router.events.on("routeChangeComplete", () => {
  jsUtils.scrollToTop();
  NProgress.done();
});
Router.events.on("routeChangeError", () => NProgress.done());

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
          <Meta />
          <ReactNotification />
          <IntercomProvider appId='ak93xau2'>
            <IntercomLoader />
            <Component {...pageProps} router={router} />
          </IntercomProvider>

        </SessionProvider>
      </ThemeProvider>
    </SWRConfig>
  );
};

const EventLoader = () => {
  const { user } = useSession();
  useEffect(() => {
    eventService.load(user);
  }, [user]);
  return null;
};

const IntercomLoader = () => {
  const { boot } = useIntercom();
  const { user } = useSession();
  useEffect(() => {
    boot({
      email: user?.email,
      name: user?.name,
    });
  }, [user]);
  return null;
};

export default App;
