import React from "react";
import { Form, Formik } from "formik";
import InputField from "@/app/components/forminputs/InputField";
import formHandler from "@/app/common/utils/formHandler";
import { IconSearch } from "@tabler/icons";
import { useSession } from "@/app/common/context/sessionContext";

const SearchForm = () => {
  const { isOss } = useSession();

  return (
    <Formik
      initialValues={{ query: "" }}
      onSubmit={formHandler(
        isOss,
        {
          id: "search_form",
          pickFieldsForEvent: ["query"],
        },
        () => Promise.reject()
      )}
    >
      <Form>
        <InputField
          type="search"
          title=""
          name="query"
          placeholder="Search"
          className="mb-0"
        />
        <IconSearch />
      </Form>
    </Formik>
  );
};
export default SearchForm;
