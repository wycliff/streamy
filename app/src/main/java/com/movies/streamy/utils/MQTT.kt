package com.movies.streamy.utils

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.security.KeyStore
import java.security.Security
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory

import java.nio.file.*;
import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;

import org.bouncycastle.jce.provider.*;
import org.bouncycastle.openssl.*;


object MQTT {
    const val QOS_AT_MOST_ONCE = 0
    const val QOS_AT_LEAST_ONCE = 1
    const val QOS_EXACTLY_ONCE = 2


    const val USER = "wycliffe" // todo : riley_driver
    const val PASSWORD = "su8UJVS5VN5CMTq"

    const val TOPIC_DRIVER_APP_UPDATES = "driver_app_updates"
    const val TOPIC_ORDERS_MOBILE_UPDATES = "orders_mobile_updates"

    private const val TOPIC_DRIVER_POSITIONS = "staging/locations"
    private const val TOPIC_DRIVER_DISPATCH_ACK = "dispatch_ack"


    const val PUSH_TYPE_DELIVERY_DISPATCH = 1
    const val PUSH_TYPE_RIDER_CONFIRMED = 3
    private const val PUSH_TYPE_DELIVERY_DATA = 1

    const val PUSH_TYPE_ERRAND_DISPATCH = "ERRAND_DISPATCH"
    const val PUSH_TYPE_ERRAND_UPDATE = "ERRAND_UPDATE"

    const val PUSH_TYPE_RIDER_COLLECTED_PACKAGE = 4
    const val PUSH_TYPE_NEW = 6
    const val PUSH_TYPE_CANCELLED = 8
    const val PUSH_TYPE_MARKED_RETURN = 9
    const val PUSH_TYPE_REALLOCATED = 11
    const val PUSH_TYPE_RESCHEDULED = 12
    const val PUSH_TYPE_UPDATE = 16
    const val PUSH_TYPE_REBEBABED = 17
    const val PUSH_TYPE_DESTINATION_UPDATED = 18
    const val PUSH_TYPE_ORDER_UPDATED = 19
    const val PUSH_TYPE_RIDER_DATA = 2
    private const val PUSH_TYPE_CLOSE_PARTNER_DATA = 3

    fun constructPositionsTopic(simCardSN: String?): String {
        return String.format("%s/%s", TOPIC_DRIVER_POSITIONS, simCardSN)
    }

    fun constructUpdatesTopic(simCardSN: String?, isMqtt: Boolean): String {
        return String.format(
            "%s%s%s",
            TOPIC_DRIVER_APP_UPDATES,
            if (isMqtt) "/" else "_",
            simCardSN
        )
    }

    fun constructOrdersTopic(transporterUuid: String?, isMqtt: Boolean): String {
        return String.format(
            "%s%s%s",
            TOPIC_ORDERS_MOBILE_UPDATES,
            if (isMqtt) "/" else "_",
            transporterUuid
        )
    }


    fun constructDispatchAckTopic(updatesTopic: String): String {
        return TOPIC_DRIVER_DISPATCH_ACK + updatesTopic.substring(updatesTopic.indexOf("/"))
    }

    fun getClientId(simCardSN: String?): String {
        return String.format("riley_driver%s", simCardSN)
    }


    fun getPushType(pushDescriptionId: Int): Int {
        if (pushDescriptionId >= 1 && pushDescriptionId <= 18) { //delivery push
            return PUSH_TYPE_DELIVERY_DATA
        } else if (pushDescriptionId >= 300 && pushDescriptionId <= 315) { //rider update
            return PUSH_TYPE_CLOSE_PARTNER_DATA
        } else if (pushDescriptionId == 1020) {
            return PUSH_TYPE_CLOSE_PARTNER_DATA
        }
        return 0
    }

    @Throws(Exception::class)
    fun getSingleSocketFactory(caCrtFileInputStream: InputStream?): SSLSocketFactory? {
        Security.addProvider(BouncyCastleProvider())
        var caCert: X509Certificate? = null
        val bis = BufferedInputStream(caCrtFileInputStream)
        val cf = CertificateFactory.getInstance("X.509")
//        while (bis.available() > 0) {
//            caCert = cf.generateCertificate(bis)
//        }

        caCert = cf.generateCertificate(caCrtFileInputStream) as X509Certificate

        val caKs = KeyStore.getInstance(KeyStore.getDefaultType())
        caKs.load(null, null)
        caKs.setCertificateEntry("cert-certificate", caCert)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(caKs)
        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext.socketFactory
    }

    @Throws(java.lang.Exception::class)
    fun getSocketFactory(
        caCrtFile: InputStream?, crtFile: InputStream?, keyFile: InputStream?,
        password: String
    ): SSLSocketFactory? {
        Security.addProvider(BouncyCastleProvider())

        // load CA certificate
        var caCert: Certificate? = null
        var bis = BufferedInputStream(caCrtFile)
        val cf = CertificateFactory.getInstance("X.509")
        while (bis.available() > 0) {
            caCert = cf.generateCertificate(bis) as Certificate
        }

        // load client certificate
        bis = BufferedInputStream(crtFile)
        var cert: Certificate? = null
        while (bis.available() > 0) {
            cert = cf.generateCertificate(bis) as Certificate
        }

        // load client private cert
        val pemParser = PEMParser(InputStreamReader(keyFile))
        val `object` = pemParser.readObject()
        val converter = JcaPEMKeyConverter().setProvider("BC")
        val key = converter.getKeyPair(`object` as PEMKeyPair)
        val caKs = KeyStore.getInstance(KeyStore.getDefaultType())
        caKs.load(null, null)
        caKs.setCertificateEntry("cert-certificate", caCert)
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(caKs)
        val ks = KeyStore.getInstance(KeyStore.getDefaultType())
        ks.load(null, null)
        ks.setCertificateEntry("certificate", cert)
        ks.setKeyEntry(
            "private-cert",
            key.private,
            password.toCharArray(),
            arrayOf<Certificate?>(cert)
        )
        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(ks, password.toCharArray())
        val context = SSLContext.getInstance("TLSv1.2")
        context.init(kmf.keyManagers, tmf.trustManagers, null)
        return context.socketFactory
    }


}

//private fun KeyStore.setCertificateEntry(s: String, caCert: X509Certificate?) {
//    val caKs = KeyStore.getInstance(KeyStore.getDefaultType())
//    caKs.load(null, null)
//    caKs.setCertificateEntry(s, caCert)
//}
