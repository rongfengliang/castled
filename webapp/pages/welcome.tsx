import Layout from "@/app/components/layout/Layout";
import Link from "next/link";

const Welcome = () => {
  return (
    <Layout title="Welcome" hideHeader>
      <h2>Set up your first pipeline</h2>
      <p>
        <Link href="/pipelines/create?demo=1">
          <a>Create Pipeline using a demo warehouse</a>
        </Link>
      </p>
      <p>
        <Link href="/pipelines/create">
          <a>Create Pipeline using your warehouse</a>
        </Link>
      </p>
    </Layout>
  );
};

export default Welcome;
