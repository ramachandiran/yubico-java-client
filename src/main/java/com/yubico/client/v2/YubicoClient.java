package com.yubico.client.v2;

import com.yubico.client.v2.impl.YubicoClientImpl;

/* Copyright (c) 2011, Linus Widströmer.  All rights reserved.
   Copyright (c) 2011, Yubico AB.  All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions
   are met:

   * Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.

   * Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following
     disclaimer in the documentation and/or other materials provided
     with the distribution.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
   CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
   INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
   MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
   DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
   BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
   TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
   ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
   TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
   THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
   SUCH DAMAGE.

   Written by Linus Widströmer <linus.widstromer@it.su.se>, January 2011.
*/

public abstract class YubicoClient {
    protected Integer clientId;
    protected String wsapi_urls[] = {"https://api.yubico.com/wsapi/2.0/verify"};

    /**
     * Validate an OTP using a webservice call to one or more ykval validation servers.
     *
     * @param otp  YubiKey OTP in modhex format
     * @return  result of the webservice validation operation
     */
    public abstract YubicoResponse verify( String otp );

    /* @see setClientId() */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * Set the ykval client identifier, used to identify the client application to
     * the validation servers. Such validation is only required for non-https-v2.0
     * validation querys, where the clientId tells the server what API key (shared
     * secret) to use to validate requests and sign responses.
     *
     * You can get a clientId and API key for the YubiCloud validation service at
     * https://upgrade.yubico.com/getapikey/
     *
     * @param clientId  ykval client identifier
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * Get the list of URLs that will be used for validating OTPs.
     * @return list of base URLs
     */
    public String[] getWsapiUrls() {
		return wsapi_urls;
	}

    /**
     * Configure what URLs to use for validating OTPs. These URLs will have
     * all the necessary parameters appended to them. Example :
     * {"https://api.yubico.com/wsapi/2.0/verify"}
     * @param wsapi  list of base URLs
     */
	public void setWsapiUrls(String[] wsapi) {
		this.wsapi_urls = wsapi;
	}

	/**
	 * Instantiate a YubicoClient object.
	 * @return  client that can be used to validate YubiKey OTPs
	 */
	public static YubicoClient getClient() {
        return new YubicoClientImpl();
    }

    /**
	 * Extract the public ID of a Yubikey from an OTP it generated.
	 *
	 * @param otp	The OTP to extract ID from, in modhex format.
	 *
	 * @return string	Public ID of Yubikey that generated otp. Between 0 and 12 characters.
	 */
	public static String getPublicId(String otp) {
		Integer len = otp.length();

		/* The OTP part is always the last 32 bytes of otp. Whatever is before that
		 * (if anything) is the public ID of the Yubikey. The ID can be set to ''
		 * through personalization.
		 */
		return otp.substring(0, len - 32);
	}
}
