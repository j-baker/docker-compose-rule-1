/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 *
 * THIS SOFTWARE CONTAINS PROPRIETARY AND CONFIDENTIAL INFORMATION OWNED BY PALANTIR TECHNOLOGIES INC.
 * UNAUTHORIZED DISCLOSURE TO ANY THIRD PARTY IS STRICTLY PROHIBITED
 *
 * For good and valuable consideration, the receipt and adequacy of which is acknowledged by Palantir and recipient
 * of this file ("Recipient"), the parties agree as follows:
 *
 * This file is being provided subject to the non-disclosure terms by and between Palantir and the Recipient.
 *
 * Palantir solely shall own and hereby retains all rights, title and interest in and to this software (including,
 * without limitation, all patent, copyright, trademark, trade secret and other intellectual property rights) and
 * all copies, modifications and derivative works thereof.  Recipient shall and hereby does irrevocably transfer and
 * assign to Palantir all right, title and interest it may have in the foregoing to Palantir and Palantir hereby
 * accepts such transfer. In using this software, Recipient acknowledges that no ownership rights are being conveyed
 * to Recipient.  This software shall only be used in conjunction with properly licensed Palantir products or
 * services.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.palantir.docker.compose.configuration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DockerComposeFilesTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void missingDockerComposeFileThrowsAnException() throws Exception {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("The following docker-compose files:");
        exception.expectMessage("does-not-exist.yaml");
        exception.expectMessage("do not exist.");
        DockerComposeFiles.from("does-not-exist.yaml");
    }

    @Test
    public void dockerComposeFileCommandGetsGeneratedCorrectly_singleComposeFile() throws Exception {
        File composeFile = tempFolder.newFile("docker-compose.yaml");
        DockerComposeFiles dockerComposeFiles = DockerComposeFiles.from(composeFile.getAbsolutePath());
        assertThat(dockerComposeFiles.constructComposeFileCommand(), is(newArrayList("-f", composeFile.getAbsolutePath())));
    }

    @Test
    public void dockerComposeFileCommandGetsGeneratedCorrectly_multipleComposeFile() throws Exception {
        File composeFile1 = tempFolder.newFile("docker-compose1.yaml");
        File composeFile2 = tempFolder.newFile("docker-compose2.yaml");
        DockerComposeFiles dockerComposeFiles = DockerComposeFiles.from(composeFile1.getAbsolutePath(), composeFile2.getAbsolutePath());
        assertThat(dockerComposeFiles.constructComposeFileCommand(), is(newArrayList(
                "-f", composeFile1.getAbsolutePath(), "-f", composeFile2.getAbsolutePath())));
    }

}