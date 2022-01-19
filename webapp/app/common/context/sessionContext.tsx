import React, { Context, useState } from "react";
import { LoggedInUserDto } from "@/app/common/dtos/LoggedInUserDto";
import authService from "@/app/services/authService";
import { AxiosResponse } from "axios";
import eventService from "@/app/services/eventService";

type SessionProviderType = {
  user: LoggedInUserDto | null | undefined;
  setUser: (session: LoggedInUserDto | null) => void;
  isOss: boolean;
  setIsOss: (mode: boolean) => void;
};

let SessionContext: Context<SessionProviderType>;
let { Provider } = (SessionContext = React.createContext<SessionProviderType>({
  user: null,
  setUser: () => {},
  isOss: false,
  setIsOss: () => {},
}));

export const useSession = () => React.useContext(SessionContext);

export default function SessionProvider({ children }: any) {
  const [user, setUser] = useState<LoggedInUserDto | null>();
  const [isOss, setIsOss] = useState<boolean>(true);
  if (user === undefined) {
    authService
      .whoAmI()
      .then((res: AxiosResponse<LoggedInUserDto>) => {
        setUser(res.data);
        if (!isOss) {
          eventService.send({
            event: "login",
          });
        }
      })
      .catch(() => {
        setUser(null);
      });
  }
  return (
    <Provider value={{ user, setUser, isOss, setIsOss }}>{children}</Provider>
  );
}
