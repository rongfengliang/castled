import { StringAnyMap } from "@/app/common/utils/types";
import http from "@/app/services/http";

export default {
  send: (props: StringAnyMap) => {
    if (process.browser) {
      http.post("/v1/tracking", props)
      .catch(() => {
        console.log(`tracking failed for event ${props.event}`)
      });
    }
  },
};
