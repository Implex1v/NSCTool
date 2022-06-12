import Head from 'next/head'
import Navbar from "./Navbar";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faDragon} from "@fortawesome/free-solid-svg-icons";

export default function Layout({children, title=""}) {
    return (
        <div className="layout">
            <Head>
                <link rel="icon" href="/favicon.ico"/>
                <meta
                    name="description"
                    content="A website for managing your NSCs of Pen and Paper games. Mainly focused on the german PnP game Das Schwarze Auge (DSA)"
                />
                <meta name="og:title" content={title}/>
                <meta name="twitter:card" content="summary_large_image"/>
                <title>NSCTool - {title}</title>
            </Head>

            <Navbar status session />

            <div className="main container-xl text-light mt-10">
                <div className="row">
                    <div className="col-xl-12">
                        <main className="mb-auto">{children}</main>
                    </div>
                </div>

                <footer className="row mt-5 mb-5">
                    <div className="col-md-12">
                        <div className="w-full mx-auto">
                            <p className="text-center">
                                <FontAwesomeIcon icon={faDragon} />
                                &copy; NSCTool 2022
                            </p>
                        </div>
                    </div>
                </footer>
            </div>
        </div>
    )
}
