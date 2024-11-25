import { redirect } from "next/navigation"
import { getServerSession } from "next-auth"

export default async function Dashboard() {

    const session = await getServerSession();

    if(!session || !session.user) {
        redirect("/login")
    }
    return <>Dashboard</>
}